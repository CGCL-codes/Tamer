package fr.alma.coeur;

import fr.alma.interfacesPlugins.PluginEntree;
import fr.alma.observation.Observateur;
import fr.alma.observation.Observer;

public class PluginEntreeBidon implements PluginEntree {

    private Observer observer;

    private static boolean stop = false;

    public PluginEntreeBidon() {
        this.observer = new Observer();
    }

    public static boolean isStopped() {
        return PluginEntreeBidon.stop;
    }

    public Class<?> getReturnType() {
        return String.class;
    }

    public String getCategorie() {
        return "test";
    }

    public String getDescription() {
        return "plugin d'entree de test";
    }

    public void start() {
        this.observer.start();
    }

    public void stop() {
        PluginEntreeBidon.stop = true;
        this.observer.stop();
    }

    public void ajoutObservateur(Observateur observateur) {
        this.observer.ajoutObservateur(observateur);
    }

    public void updateObservateurs() {
        this.observer.updateObservateurs("test");
    }
}
