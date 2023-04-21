package br.com.rmt.ganttprogramming.leitor;

import br.com.rmt.ganttprogramming.Projeto;
import br.com.rmt.ganttprogramming.Tarefa;
import br.com.rmt.ganttprogramming.anotacoes.AParametro;
import br.com.rmt.ganttprogramming.anotacoes.ATarefa;
import br.com.rmt.ganttprogramming.anotacoes.AProjeto;
import br.com.rmt.ganttprogramming.anotacoes.ASomenteLocal;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public abstract class CriarProjeto<T extends Projeto> implements Serializable {

    public abstract T novoProjeto();

    public Method buscarTarefaPeloNome(Class classe, String nome) throws Exception {
        final Object objeto = classe.newInstance();
        for (Method metodo : classe.getMethods()) {
            ATarefa aTarefa = (ATarefa) metodo.getAnnotation(ATarefa.class);
            if (aTarefa != null && nome.equals(aTarefa.nome())) {
                return metodo;
            }
        }
        return null;
    }

    public Tarefa lerTarefa(Class classe, String nome) throws Exception {
        final Object objeto = classe.newInstance();
        for (final Method metodo : classe.getMethods()) {
            ATarefa aTarefa = (ATarefa) metodo.getAnnotation(ATarefa.class);
            if (aTarefa != null && nome.equals(aTarefa.nome())) {
                Tarefa tarefa = this.tarefa(aTarefa, metodo, objeto, null);
                return tarefa;
            }
        }
        return null;
    }

    public Tarefa tarefa(ATarefa aTarefa, final Method metodo, final Object objeto, final T projeto) {
        Tarefa tarefa = new Tarefa() {

            @Override
            public Map<String, Serializable> funcao(Map<String, Serializable> argumentos) {
                try {
                    Map<String, Serializable> resultados = null;
                    Object resultadoNaoTratado = null;
                    if (metodo.getParameterTypes().length > 0) {
                        Class parametro1 = metodo.getParameterTypes()[0];
                        if (metodo.getParameterTypes().length == 1 && parametro1 == Map.class) {
                            resultadoNaoTratado = metodo.invoke(objeto, argumentos);
                        } else {
                            Object[] argsTratados = new Object[metodo.getParameterTypes().length];
                            Annotation[][] anotacoes = metodo.getParameterAnnotations();
                            int contador = 0;
                            for (Class tipoParametro : metodo.getParameterTypes()) {
                                Annotation anotacao = anotacoes[contador][0];
                                if (anotacao instanceof AParametro) {
                                    AParametro aParametro = (AParametro) anotacao;
                                    if ("projeto".equals(aParametro.value())) {
                                        argsTratados[contador] = projeto;
                                        this.setExecutarApenasLocal(true);
                                    } else argsTratados[contador] = argumentos.get(aParametro.value());
                                }
                                contador++;
                            }
                            resultadoNaoTratado = metodo.invoke(objeto, argsTratados);
                        }
                    } else {
                        resultadoNaoTratado = metodo.invoke(objeto);
                    }
                    if (resultadoNaoTratado instanceof Map) {
                        resultados = (Map<String, Serializable>) resultadoNaoTratado;
                    } else {
                        resultados = new HashMap<String, Serializable>();
                        resultados.put("_tarefa_" + this.getNome(), (Serializable) resultadoNaoTratado);
                    }
                    return resultados;
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                return null;
            }
        };
        tarefa.setNome(aTarefa.nome());
        ASomenteLocal aSomenteLocal = (ASomenteLocal) metodo.getAnnotation(ASomenteLocal.class);
        if (aSomenteLocal != null && aSomenteLocal.value() == true) tarefa.setExecutarApenasLocal(true);
        return tarefa;
    }

    public T ler(Class classe) throws Exception {
        T projeto = novoProjeto();
        final Object objeto = classe.newInstance();
        AProjeto plano = (AProjeto) classe.getAnnotation(AProjeto.class);
        projeto.setNome(plano.nome());
        class TarefaEAnotacao {

            public Tarefa tarefa;

            public ATarefa aTarefa;

            public TarefaEAnotacao(Tarefa tarefa, ATarefa aTarefa) {
                this.tarefa = tarefa;
                this.aTarefa = aTarefa;
            }
        }
        Map<String, TarefaEAnotacao> tarefas = new HashMap<String, TarefaEAnotacao>();
        for (final Method metodo : classe.getMethods()) {
            ATarefa aTarefa = (ATarefa) metodo.getAnnotation(ATarefa.class);
            if (aTarefa != null) {
                Tarefa tarefa = this.tarefa(aTarefa, metodo, objeto, projeto);
                tarefas.put(tarefa.getNome(), new TarefaEAnotacao(tarefa, aTarefa));
                projeto.adicionarTarefa(tarefa);
            }
        }
        for (TarefaEAnotacao composicao : tarefas.values()) {
            if (composicao.aTarefa.dependencias() != null && composicao.aTarefa.dependencias().length > 0) {
                for (String nomeDep : composicao.aTarefa.dependencias()) {
                    TarefaEAnotacao composicaoDep = tarefas.get(nomeDep);
                    if (composicaoDep != null) {
                        Tarefa tarefaDep = composicaoDep.tarefa;
                        projeto.adicionarDependencia(composicao.tarefa, tarefaDep);
                    }
                }
            }
        }
        return projeto;
    }
}
