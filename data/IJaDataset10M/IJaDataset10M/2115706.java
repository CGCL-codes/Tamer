package com.businesslayerbuilder.web.front.actions;

import java.lang.reflect.Method;
import java.util.Hashtable;
import com.businesslayerbuilder.exception.BusinessLayerException;
import com.businesslayerbuilder.web.front.teste.Teste;

/**
 * Classe Decypher<BR>
 * Classe respons�vel por "decrifrar" quais a��es dever ser tomadas quando uma URL � requisitada.<BR>
 * Criada em 04/09/2007. <BR>
 * @author Rafael Bernardo
 */
public final class Decypher {

    /** decypher: instancia do pattern Singletton */
    private static Decypher decypher = null;

    /**
     * Construtor privado para obedeccer ao patterns Singletton.
     */
    private Decypher() {
        this.actions = new Hashtable<String, ActionClass>();
    }

    /**
     * Obtem a instancia �nica do objeto <code>Decypher</code>
     * @return A instancia �nica do objeto <code>Decypher</code>
     * @see Decypher
     */
    public static Decypher getDecypher() {
        if (Decypher.decypher == null) {
            Decypher.decypher = new Decypher();
        }
        return decypher;
    }

    /**
     * M�todo loadActions
     * @param file_path
     * @return <code>true</code> se as configura��es de Actions foram carregadas com sucesso; <code>false</code> caso contrario.
     */
    public boolean loadActionsConfig(String file_path) {
        try {
            System.out.println(file_path);
            ActionClass action = new ActionClass(Teste.class.getCanonicalName(), "", "retornaQualquerCoisa", new Parameter[] { new Parameter("nome", String.class, true), new Parameter("id", Integer.class, true, new ValidatorRange(5, 6000)) });
            this.actions.put("/businesslayerbuilderWEB/teste1.blbw", action);
            ActionClass action2 = new ActionClass(Teste.class.getCanonicalName(), "", "retornaQualquerCoisa", new Parameter[] { new Parameter("nome", String.class, true), new Parameter("id", Integer.class, true, new ValidatorRange(5, 6000)), new Parameter("id2", Float.class, false, new ValidatorRange(0.1, 0.600)) });
            this.actions.put("/businesslayerbuilderWEB/teste2.blbw", action2);
            ActionClass action3 = new ActionClass(Teste.class.getCanonicalName(), "index_result.jsp", "index.jsp", "testeDados", new Parameter[] { new Parameter("nome", String.class, true), new Parameter("idade", Integer.class, true, new ValidatorRange(0, 120)) });
            this.actions.put("/businesslayerbuilderWEB/dados.blbw", action3);
        } catch (BusinessLayerException exception) {
            exception.printStackTrace();
        }
        return false;
    }

    /** actions: Mapa com as a��es que devem ser tomadas tendo a URL requisitada como chave. */
    private Hashtable<String, ActionClass> actions;

    /**
     * Decifra qual � a a��o configurada para a URL informada em <code>requested_url</code>.
     * @param requested_url A URL que foi requisitada.
     * @return A a��o que deve ser tomada.
     * @see ActionClass
     */
    public ActionClass getActionForURL(String requested_url) {
        return this.actions.get(requested_url);
    }

    /**
     * Descobre qual m�todo da classe dever� ser o m�todo invocado na a��o.
     * @param method_name O nome da opera��o (m�todo)
     * @param class_obj A classe que possui a declara��o do m�todo.
     * @param parameters a lista de parametros que deve ser passada para o m�doto.
     * @return O m�todo mais comp�tivel.
     * @see Method
     */
    public static Method discoverMethod(String method_name, Class<?> class_obj, Parameter... parameters) {
        for (Method m : class_obj.getDeclaredMethods()) {
            if (m.getName().equals(method_name)) {
                if (Decypher.isParametersMatching(m, parameters)) {
                    return m;
                }
            }
        }
        return null;
    }

    /**
     * Verifica se a lista de parametros passada em <code>parameters</code> se encaixa nos parametros
     * necess�rios � invoca��o do m�todo informado em <code>method</code>.
     * @param method O m�todo que poder� ser invocado.
     * @param parameters os parametros a serem passados para o m�todo.
     * @return <code>true</code> caso sejam compat�veis. <code>false</code> caso contr�rio.
     * @see Class#getCanonicalName()
     */
    private static boolean isParametersMatching(Method method, Parameter... parameters) {
        Class<?>[] types = method.getParameterTypes();
        if (types.length == parameters.length) {
            for (int i = 0; i < parameters.length; i++) {
                if (!types[i].getCanonicalName().equals(parameters[i].getParameterType().getCanonicalName())) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
}
