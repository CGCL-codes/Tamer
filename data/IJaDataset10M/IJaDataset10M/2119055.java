package siouxsie.desktop.commands;

import java.util.Collection;
import javax.swing.JComponent;
import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.OrderedConfiguration;
import siouxsie.desktop.IStatusBar;
import siouxsie.ioc.IServiceStartUpBus;
import siouxsie.ioc.IServiceStartUpListener;
import siouxsie.ioc.ServiceStartUpBus;

/**
 * @author Arnaud Cogoluegnes
 * @version $Id: ContributionMenuModule.java 189 2008-10-21 15:09:06Z acogo $
 */
public class NoEmptyMenuBecauseOfSubMenusModule {

    public static void contributeFaceRegistry(OrderedConfiguration<Face> configuration) {
        configuration.add("accueil", new Face().commandId("accueil").groupId("general").text("Accueil"));
        configuration.add("console", new Face().commandId("console").groupId("general").text("Console"));
        configuration.add("about", new Face().commandId("about").groupId("help").text("A propos"));
        configuration.add("commande.recherche", new Face().commandId("commande.recherche").groupId("commande").text("Recherche"));
        configuration.add("commande.creation", new Face().commandId("commande.creation").groupId("commande").text("Cr�ation"));
        configuration.add("livraisons", new Face().commandId("livraisons").groupId("livraison.livraison.plateforme").text("Livraisons"));
        configuration.add("reclamations", new Face().commandId("reclamations").groupId("livraison.livraison.plateforme").text("R�clamations"));
        configuration.add("receptions", new Face().commandId("receptions").groupId("livraison.livraison.directe").text("R�ceptions"));
        configuration.add("fournisseurs", new Face().commandId("fournisseurs").groupId("livraison.livraison.directe").text("Fournisseurs"));
        configuration.add("articles.locaux", new Face().commandId("articles.locaux").groupId("parametre").text("Articles locaux"));
    }

    public static void contributeGroupRegistry(OrderedConfiguration<Group> configuration) {
        configuration.add("general", new Group().id("general").text("G�n�ral"));
        configuration.add("commande", new Group().id("commande").text("Commande"));
        configuration.add("livraison", new Group().id("livraison").text("Livraison"));
        configuration.add("livraison.livraison.plateforme", new Group().id("livraison.livraison.plateforme").parentId("livraison").text("Liv. plateforme"));
        configuration.add("livraison.livraison.directe", new Group().id("livraison.livraison.directe").parentId("livraison").text("Liv. directe"));
        configuration.add("parametre", new Group().id("parametre").text("Param�tre"));
        configuration.add("help", new Group().id("help").text("Aide"));
    }

    public static void contributeCommandRegistry(Configuration<CommandDescription> configuration) {
        configuration.add(new CommandDescription("accueil", new DummyCommand("accueil")));
        configuration.add(new CommandDescription("console", new DummyCommand("console")));
        configuration.add(new CommandDescription("about", new DummyCommand("about")));
        configuration.add(new CommandDescription("commande.recherche", "appro", new DummyCommand("commande.recherche")));
        configuration.add(new CommandDescription("commande.creation", "appro", new DummyCommand("commande.creation")));
        configuration.add(new CommandDescription("livraisons", "logistique", new DummyCommand("livraisons")));
        configuration.add(new CommandDescription("reclamations", "logistique", new DummyCommand("reclamations")));
        configuration.add(new CommandDescription("receptions", "logistique", new DummyCommand("receptions")));
        configuration.add(new CommandDescription("fournisseurs", "logistique", new DummyCommand("fournisseurs")));
        configuration.add(new CommandDescription("livraisons", "logistique", new DummyCommand("livraisons")));
        configuration.add(new CommandDescription("articles.locaux", "logistique", new DummyCommand("articles.locaux")));
    }

    public static IStatusBar buildStatusBar() {
        return new IStatusBar() {

            public JComponent getComponent() {
                return null;
            }

            public void processEnded() {
            }

            public void processStarted(boolean indeterminate) {
            }

            public void setLeadingMessage(String msg) {
            }

            public void setProgress(int progress) {
            }

            public void setTrailingMessage(String msg) {
            }
        };
    }

    public static IServiceStartUpBus buildServiceStartUpBus(Collection<IServiceStartUpListener> lsts) {
        return new ServiceStartUpBus(lsts);
    }
}
