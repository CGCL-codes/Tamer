package fr.insa.rennes.pelias.pexecutor.services;

import java.util.ArrayList;
import java.util.List;
import com.sun.webui.jsf.model.Option;
import fr.insa.rennes.pelias.platform.LocalJobScheduler;
import fr.insa.rennes.pelias.platform.SGEJobScheduler;

/**
 *
 * @author 2bo
 */
public class JobSchedulerEnumerator {

    private static String[] options = { "SGE", "Local" };

    /**
     * Retourne la liste des Schedulers sous forme d'une liste d'options.
     * La valeur associé à l'otion est son index dans le tableau options.
     * @return la liste des JobScheduler
     */
    public static List getOptions() {
        List returnValue = new ArrayList();
        for (int i = 0; i < options.length; i++) {
            String elem = options[i];
            Option option = new Option(i, elem);
            returnValue.add(option);
        }
        return returnValue;
    }

    /**
     * Prend en paramètre l'index d'un JobScheduler dans une liste d'option
     * peuplée à l'aide de getOptions et retourne la valeur de la classe
     * associée au JobScheduler en question
     * @param i index du JobScheduler
     * @return la classe du JobScheduler
     */
    public Class<?> getJobScheduler(int i) {
        switch(i) {
            case 0:
                return SGEJobScheduler.class;
            case 1:
                return LocalJobScheduler.class;
            default:
                return null;
        }
    }

    /**
     * En fonction d'une classe retourne l'index du JobScheduler dans la liste
     * d'options
     * @param c la classe du JobScheduler
     * @return l'index correspondant au JobScheduler
     */
    public Integer getIdJobScheduler(Class<?> c) {
        if (c.equals(SGEJobScheduler.class)) {
            return 0;
        }
        if (c.equals(LocalJobScheduler.class)) {
            return 1;
        }
        return null;
    }

    /**
     * @param aOptions the options to set
     */
    public static void setOptions(String[] aOptions) {
        options = aOptions;
    }

    public JobSchedulerEnumerator() {
    }
}
