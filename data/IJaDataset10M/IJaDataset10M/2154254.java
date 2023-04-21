package parserMetrics.javaStructures;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import parserMetrics.metrics.Context;
import parserMetrics.metrics.ContextoAtual;
import org.sablecc.java.node.Start;

public class Classe {

    private String nome;

    private ArrayList<String> extendsList;

    private ArrayList<String> implementsList;

    private ArrayList<Metodo> metodos;

    private ArrayList<Construtor> construtores;

    private ArrayList<Atributo> atributos;

    public Metodo currentMethod;

    public Construtor currentConstructor;

    private String fileName;

    private Start ast;

    private ArrayList<String> listaCatches;

    private HashMap<String, String> declaracoes;

    private HashMap<String, Integer> instanciacaoDeTipos;

    public Set<String> outrosTiposUsados;

    public HashMap<String, Set<String>> metodosInvocados;

    public HashMap<String, Set<String>> atributosAcessados;

    public Classe(String nome, String fileName) {
        this(nome);
        this.fileName = fileName;
    }

    public Classe(String nome, Class rClass) {
        this(nome);
        Method[] ms = rClass.getMethods();
        for (Method m : ms) {
            Metodo m1 = new Metodo();
            m1.setIdentificador(m.getName());
            m1.setTipoRetorno(m.getReturnType().getSimpleName());
            metodos.add(m1);
        }
        Field[] fs = rClass.getFields();
        for (Field f : fs) {
            boolean isStatic = Modifier.isStatic(f.getModifiers());
            Atributo a = getOrCreateAtributo(f.getName(), f.getType().getSimpleName(), isStatic);
            a.ocorrencias = a.ocorrencias + 1;
        }
    }

    public Classe(String nome) {
        this.metodosInvocados = new HashMap<String, Set<String>>();
        this.atributosAcessados = new HashMap<String, Set<String>>();
        this.outrosTiposUsados = new HashSet<String>();
        this.instanciacaoDeTipos = new HashMap<String, Integer>();
        this.extendsList = new ArrayList<String>();
        this.implementsList = new ArrayList<String>();
        this.nome = nome;
        this.metodos = new ArrayList<Metodo>();
        this.construtores = new ArrayList<Construtor>();
        this.atributos = new ArrayList<Atributo>();
        this.listaCatches = new ArrayList<String>();
        this.declaracoes = new HashMap<String, String>();
    }

    public ArrayList<Atributo> getAtributos() {
        return atributos;
    }

    public ArrayList<Metodo> getMetodos() {
        return metodos;
    }

    public ArrayList<Construtor> getConstrutores() {
        return construtores;
    }

    public String getFileName() {
        return fileName;
    }

    public String getNome() {
        return nome;
    }

    public Start getAst() {
        return ast;
    }

    public void setAst(Start ast) {
        this.ast = ast;
    }

    private String getAtributosAsString() {
        String ret = "Par�metros: ";
        return ret;
    }

    public String toString() {
        return "### CLASS ###" + " - \n" + "Nome: " + nome + " - \n" + getAtributosAsString() + "\n" + "### END CLASS ###";
    }

    public void abreMetodo(String metodo) {
        Metodo m = null;
        for (Metodo tmp : metodos) {
            if (tmp.getIdentificador().equals(metodo)) m = tmp;
        }
        currentMethod = m;
        Context.getInstance().contextoAtual = ContextoAtual.METODO;
    }

    public void abreConstrutor(Construtor construtor) {
        Construtor c = null;
        int i = construtores.indexOf(construtor);
        c = construtores.get(i);
        currentConstructor = c;
        Context.getInstance().contextoAtual = ContextoAtual.CONSTRUTOR;
    }

    public Metodo getMetodo(String nome) {
        for (Metodo m : metodos) {
            if (m.getIdentificador().equals(nome)) {
                return m;
            }
        }
        if (!extendsList.isEmpty()) {
            for (String superclasse : extendsList) {
                Classe c = Context.getInstance().getClasse(superclasse);
                if (c != null) {
                    return c.getMetodo(nome);
                }
            }
        }
        return null;
    }

    public ArrayList<String> getExtendsList() {
        return extendsList;
    }

    public void setExtendsList(ArrayList<String> extendsList) {
        this.extendsList = extendsList;
    }

    public String getTipoAtributo(String nome) {
        for (Atributo a : atributos) {
            if (a.nome.equals(nome)) {
                return a.tipo;
            }
        }
        if (!extendsList.isEmpty()) {
            for (String superclasse : extendsList) {
                Classe c = Context.getInstance().getClasse(superclasse);
                if (c != null) {
                    return c.getTipoAtributo(nome);
                }
            }
        }
        return null;
    }

    public boolean isAtributoStatic(String nome) {
        for (Atributo a : atributos) {
            if (a.nome.equals(nome)) {
                return a.isStatic;
            }
        }
        if (!extendsList.isEmpty()) {
            for (String superclasse : extendsList) {
                Classe c = Context.getInstance().getClasse(superclasse);
                if (c != null) {
                    return c.isAtributoStatic(nome);
                }
            }
        }
        return false;
    }

    public String getTipoRetornoMetodo(String nome) {
        for (Metodo m : metodos) {
            if (m.getIdentificador().equals(nome)) {
                return m.getTipoRetorno();
            }
        }
        if (!extendsList.isEmpty()) {
            for (String superclasse : extendsList) {
                Classe c = Context.getInstance().getClasse(superclasse);
                if (c != null) {
                    return c.getTipoRetornoMetodo(nome);
                }
            }
        }
        return null;
    }

    public HashMap<String, String> getDeclaracoes() {
        return declaracoes;
    }

    public ArrayList<String> getListaCatches() {
        return listaCatches;
    }

    public ArrayList<String> getImplementsList() {
        return implementsList;
    }

    public HashMap<String, Integer> getInstanciacaoDeTipos() {
        return instanciacaoDeTipos;
    }

    public String relatorioMetricas() {
        String ret = "######################### CLASS #########################\n";
        ret += "Classe: " + nome + "\n";
        ret += "Extends: ";
        for (String s : extendsList) {
            ret += s + ", ";
        }
        ret += "\n";
        ret += "Implements: ";
        for (String s : implementsList) {
            ret += s + ", ";
        }
        ret += "\n";
        ret += "Tipos instanciados: ";
        Set<String> setInst = instanciacaoDeTipos.keySet();
        for (String s : setInst) {
            ret += s + "(" + instanciacaoDeTipos.get(s) + "), ";
        }
        ret += "\n";
        for (Metodo m : metodos) {
            ret += m.relatorioMetricas();
        }
        ret += "\n";
        return ret;
    }

    public String relatorioSimplificadoMetricas() {
        String ret = "######################### CLASS #########################\n";
        ret += "Classe: " + nome + "\n";
        ret += "Extends: ";
        for (String s : extendsList) {
            ret += s + ", ";
        }
        ret += "\n";
        ret += "Implements: ";
        for (String s : implementsList) {
            ret += s + ", ";
        }
        ret += "\n";
        ret += "Tipos instanciados: ";
        HashMap<String, Integer> todasInstanciacoes = getTodasInstanciacoes();
        Set<String> setInst = todasInstanciacoes.keySet();
        for (String s : setInst) {
            ret += s + "(" + todasInstanciacoes.get(s) + "), ";
        }
        ret += "\n";
        ret += "NOA: " + getNOA() + "\n";
        ret += "NOO: " + getNOO() + "\n";
        ret += "WOC: " + getWOC() + "\n";
        ret += "Coupled To: ";
        for (String s : coupledTo()) {
            ret += s + ", ";
        }
        ret += "\n";
        ret += "LCOO: " + getLCOO() + "\n";
        ret += "\n";
        return ret;
    }

    private HashMap<String, Integer> getTodasInstanciacoes() {
        HashMap<String, Integer> ret = instanciacaoDeTipos;
        for (Metodo m : metodos) {
            ret = mergeInstanciacoes(ret, m.getInstanciacaoDeTipos());
        }
        for (Construtor c : construtores) {
            ret = mergeInstanciacoes(ret, c.getInstanciacaoDeTipos());
        }
        return ret;
    }

    private HashMap<String, Integer> mergeInstanciacoes(HashMap<String, Integer> i1, HashMap<String, Integer> i2) {
        HashMap<String, Integer> ret = new HashMap<String, Integer>();
        Set<String> s1 = i1.keySet();
        for (String s : s1) {
            ret.put(s, i1.get(s));
        }
        Set<String> s2 = i2.keySet();
        for (String s : s2) {
            if (ret.containsKey(s)) {
                int qtd = ret.get(s) + i2.get(s);
                ret.put(s, qtd);
            } else {
                ret.put(s, i2.get(s));
            }
        }
        return ret;
    }

    public Atributo getAtributo(String nome, String tipo) {
        Atributo a = new Atributo(nome, tipo, false);
        int i = atributos.indexOf(a);
        if (i == -1) {
            return null;
        } else {
            return atributos.get(i);
        }
    }

    public boolean hasAtributo(String nome, String tipo) {
        return getAtributo(nome, tipo) != null;
    }

    public Atributo getOrCreateAtributo(String nome, String tipo, boolean isStatic) {
        Atributo a = getAtributo(nome, tipo);
        if (a == null) {
            a = new Atributo(nome, tipo, isStatic);
            atributos.add(a);
        }
        return a;
    }

    public int getNOA() {
        int noa = 0;
        HashSet<String> nomeAtributo = new HashSet<String>();
        for (Atributo a : atributos) {
            if (!nomeAtributo.contains(a.nome)) {
                nomeAtributo.add(a.nome);
                noa++;
            }
        }
        return noa;
    }

    public int getNOO() {
        return metodos.size();
    }

    public int getWOC() {
        int woc = 0;
        for (Metodo m : metodos) {
            woc += m.getListaParametros().size() + 1;
        }
        return woc;
    }

    public int getLCOO() {
        Set<ParDeMetodo> p = new HashSet<ParDeMetodo>();
        Set<ParDeMetodo> q = new HashSet<ParDeMetodo>();
        for (Metodo m1 : metodos) {
            for (Metodo m2 : metodos) {
                if (!m1.equals(m2)) {
                    Set<String> a1 = m1.getAtributosAcessados(this.nome);
                    Set<String> a2 = m2.getAtributosAcessados(this.nome);
                    if (a1 == null || a2 == null) {
                        p.add(new ParDeMetodo(m1, m2));
                    } else {
                        Set<String> a1_tmp = new HashSet<String>();
                        Set<String> a2_tmp = new HashSet<String>();
                        for (String s : a1) {
                            if (!isAtributoStatic(s)) {
                                a1_tmp.add(s);
                            }
                        }
                        for (String s : a2) {
                            if (!isAtributoStatic(s)) {
                                a2_tmp.add(s);
                            }
                        }
                        if (Collections.disjoint(a1_tmp, a2_tmp)) {
                            p.add(new ParDeMetodo(m1, m2));
                        } else {
                            q.add(new ParDeMetodo(m1, m2));
                        }
                    }
                }
            }
        }
        return (p.size() > q.size()) ? p.size() - q.size() : 0;
    }

    public Set<String> coupledTo() {
        Set<String> coupledSet = outrosTiposUsados;
        for (String s : listaCatches) {
            coupledSet.add(s);
        }
        Set<String> declaracoesKeys = declaracoes.keySet();
        for (String s : declaracoesKeys) {
            coupledSet.add(declaracoes.get(s));
        }
        Set<String> tiposInstanciados = instanciacaoDeTipos.keySet();
        for (String s : tiposInstanciados) {
            coupledSet.add(s);
        }
        Set<String> metodosInvocadosKeys = metodosInvocados.keySet();
        for (String s : metodosInvocadosKeys) {
            coupledSet.add(s);
        }
        Set<String> atributosAcessadosKeys = atributosAcessados.keySet();
        for (String s : atributosAcessadosKeys) {
            coupledSet.add(s);
        }
        for (String s : extendsList) {
            coupledSet.add(s);
        }
        for (String s : implementsList) {
            coupledSet.add(s);
        }
        for (Atributo a : atributos) {
            coupledSet.add(a.tipo);
        }
        for (Metodo m : metodos) {
            coupledSet.addAll(m.coupledTo());
        }
        for (Construtor c : construtores) {
            coupledSet.addAll(c.coupledTo());
        }
        return coupledSet;
    }
}
