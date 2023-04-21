package certi.simul.grafcet;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;

/**
 * 
 * Created on Oct 11, 2003
 * 
 * @author ilm 
 */
public class InterpretadorGrafcet implements Runnable {

    /** Flag que indica se passo de inicializacao foi realizado */
    private boolean iniciado = false;

    /** Intervalo entre cada passo de execucao, usado pela Thread */
    private volatile int tempoCicloMs = 1000;

    /** Intervalo entre cada passo de execucao, usado pela Thread */
    private AnalisadorGrafcet analisador;

    private Transicao[] transicoes;

    private Etapa[] etapas;

    /** Objetos passados as transicoes em somente um ciclo */
    private LinkedList dadoCiclo = new LinkedList();

    /** Array objetos passados as transicoes em somente um ciclo */
    private Object[] dadoCicloArray = new Object[1];

    /** Monitor do grafcet */
    private final MonitorGrafcet monitor;

    /** Construtor */
    public InterpretadorGrafcet(PrototipoGrafcet grafcet) throws ExcecaoNaValidacao {
        this(grafcet, null);
    }

    /** Construtor */
    public InterpretadorGrafcet(PrototipoGrafcet grafcet, MonitorGrafcet m) throws ExcecaoNaValidacao {
        analisador = new AnalisadorGrafcet(grafcet);
        analisador.validar();
        etapas = analisador.obterGrafcet().etapas;
        transicoes = analisador.obterGrafcet().transicoes;
        monitor = m;
    }

    /** Primeiro passo: aciona etapas inicias */
    private boolean inicializacao() {
        if (!analisador.estaValidado()) {
            try {
                analisador.validar();
                etapas = analisador.obterGrafcet().etapas;
                transicoes = analisador.obterGrafcet().transicoes;
            } catch (ExcecaoNaValidacao e) {
                e.printStackTrace();
                return false;
            }
        }
        for (int i = 0; i < etapas.length; i++) {
            if (etapas[i].ehInicial) {
                etapas[i].estado = Etapa.ATIVA;
                if (monitor != null) monitor.etapaAtiva(true, etapas[i].numero);
                chamarAcao(true, etapas[i]);
            } else etapas[i].estado = Etapa.INATIVA;
            etapas[i].inicializa();
        }
        return iniciado = true;
    }

    /** Realiza um ciclo do grafcet */
    public synchronized boolean passo() {
        boolean ok = true;
        if (monitor != null) monitor.cicloIniciado(true);
        if (!iniciado) return inicializacao();
        synchronized (dadoCiclo) {
            if (dadoCiclo.size() > 0) {
                dadoCicloArray[0] = dadoCiclo.toArray(new Object[dadoCiclo.size()]);
                dadoCiclo.clear();
            }
        }
        for (int i = 0; i < transicoes.length; i++) {
            transicoes[i].valida = etapasAtivas(transicoes[i].etapasAnteriores);
            boolean receptividade = false;
            if (transicoes[i].valida) receptividade = chamarReceptividade(transicoes[i]);
            transicoes[i].disparar = receptividade && transicoes[i].valida;
            if (transicoes[i].disparar) {
                for (int j = 0; j < transicoes[i].etapasPosteriores.length; j++) transicoes[i].etapasPosteriores[j].proximoEstado = Etapa.ATIVA;
                for (int j = 0; j < transicoes[i].etapasAnteriores.length; j++) {
                    int estadoEtapa = transicoes[i].etapasAnteriores[j].proximoEstado;
                    if (estadoEtapa != Etapa.ATIVA) transicoes[i].etapasAnteriores[j].proximoEstado = Etapa.INATIVA;
                }
            }
            if (transicoes[i].disparar && monitor != null) monitor.transicaoDisparada(transicoes[i].numero);
        }
        for (int i = 0; i < etapas.length; i++) {
            if (etapas[i].estado == Etapa.ATIVA && etapas[i].proximoEstado == Etapa.INATIVA) {
                etapas[i].estado = Etapa.INATIVA;
                if (etapas[i].cicloAcao < 0) etapas[i].desacionar = true;
                if (monitor != null) monitor.etapaAtiva(false, etapas[i].numero);
            }
        }
        for (int i = 0; i < etapas.length; i++) {
            if (etapas[i].estado == Etapa.INATIVA && etapas[i].proximoEstado == Etapa.ATIVA) {
                etapas[i].estado = Etapa.ATIVA;
                etapas[i].cicloAcao = (etapas[i].demoraAcao / tempoCicloMs);
                if (monitor != null) monitor.etapaAtiva(true, etapas[i].numero);
            }
            if (etapas[i].estado == Etapa.ATIVA) {
                if (etapas[i].cicloAcao == 0) {
                    etapas[i].acionar = true;
                } else if (etapas[i].cicloAcao < 0) if (etapas[i].ehAcaoContinua) etapas[i].acionar = true;
                etapas[i].cicloAcao--;
            }
        }
        for (int i = 0; i < etapas.length; i++) if (etapas[i].desacionar) chamarAcao(false, etapas[i]);
        for (int i = 0; i < etapas.length; i++) if (etapas[i].acionar) chamarAcao(true, etapas[i]);
        for (int i = 0; i < etapas.length; i++) {
            etapas[i].proximoEstado = Etapa.IMODIFICADA;
            etapas[i].acionar = false;
            etapas[i].desacionar = false;
        }
        if (monitor != null) monitor.cicloIniciado(false);
        dadoCicloArray[0] = null;
        return ok;
    }

    /**
	 * 
	 */
    public void aguardar() {
        synchronized (this) {
            try {
                this.wait(tempoCicloMs);
            } catch (InterruptedException e) {
            }
        }
    }

    /**
	 * @param etapas
	 * @return true se todas etapas ativas no array
	 */
    private boolean etapasAtivas(Etapa[] etapas) {
        boolean ativas = true;
        for (int i = 0; i < etapas.length; i++) ativas &= (etapas[i].estado == Etapa.ATIVA);
        return ativas;
    }

    /**
	 * @param transicao
	 * @return boolean o valor da receptividade
	 */
    private boolean chamarReceptividade(Transicao transicao) {
        Method method = transicao.receptividade;
        Class[] param = method.getParameterTypes();
        try {
            if (param.length == 0) {
                Boolean b = (Boolean) method.invoke(analisador.obterPrototipo(), null);
                return b.booleanValue();
            }
            if (param.length == 1) {
                Boolean b = (Boolean) method.invoke(analisador.obterPrototipo(), dadoCicloArray);
                return b.booleanValue();
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            if (monitor != null) monitor.tratarExcecao(e, false, transicao.numero);
        }
        return false;
    }

    /**
	 * 
	 * @param acao
	 * @param etap
	 */
    private void chamarAcao(boolean acao, Etapa etap) {
        Method method = (acao) ? etap.metodoAcao : etap.metodoDesacao;
        if (method == null) return;
        Class[] param = method.getParameterTypes();
        try {
            if (monitor != null && acao) monitor.etapaAcionada(etap.numero);
            if (param.length == 0) {
                method.invoke(analisador.obterPrototipo(), null);
            } else if (param.length == 1) {
                method.invoke(analisador.obterPrototipo(), dadoCicloArray);
            } else throw new IllegalArgumentException("N�mero de argumentos inv�lidos na fun��o de a��o ou desa��o: " + param.length + ", etapa: " + etap.numero);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            if (monitor != null) monitor.tratarExcecao(e, true, etap.numero);
        }
    }

    /**
	 * Reseta grafcet, desacionando etapas ativas
	 */
    public synchronized void reset() {
        if (!iniciado) return;
        for (int i = 0; i < etapas.length; i++) {
            if (etapas[i].estado == Etapa.ATIVA) {
                if (etapas[i].cicloAcao < 0) etapas[i].desacionar = true;
                if (monitor != null) monitor.etapaAtiva(false, etapas[i].numero);
            }
        }
        for (int i = 0; i < etapas.length; i++) {
            if (etapas[i].desacionar) chamarAcao(false, etapas[i]);
        }
        iniciado = false;
    }

    public void run() {
        while (true) {
            passo();
            synchronized (this) {
                try {
                    this.wait(tempoCicloMs);
                } catch (InterruptedException e) {
                    return;
                }
            }
        }
    }

    /**
	 * @return int o tempo de ciclo em milisegundos
	 */
    public int obterTempoCicloMs() {
        return tempoCicloMs;
    }

    /**
	 * @return Grafcet o grafcet associado ao motor
	 */
    public Grafcet obterGrafcet() {
        return analisador.obterGrafcet();
    }

    /**
	 * Seta o tempo de ciclo, este tempo nao pode ser igual a zero. O
	 * m�nimo recomendado eh de 50ms
	 * @param tc
	 */
    public void setarTempoCicloMs(int tc) {
        if (tc <= 0) tempoCicloMs = 50; else tempoCicloMs = tc;
    }

    /**
	 * @param obj o objeto de dado a ser adicionado ao ciclo
	 */
    public void adicionarDadoCiclo(Object obj) {
        synchronized (dadoCiclo) {
            dadoCiclo.add(obj);
        }
    }

    /**
	 * @param objs 
	 */
    public void adicionarDadoCiclo(Object[] objs) {
        synchronized (dadoCiclo) {
            for (int i = 0; i < objs.length; i++) dadoCiclo.add(objs[i]);
        }
    }

    /**
	 * @return AnalisadorGrafcet
	 */
    public AnalisadorGrafcet obterAnalisador() {
        return analisador;
    }
}
