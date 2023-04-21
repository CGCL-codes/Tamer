package fr.ifsic.film.client.ui;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import fr.ifsic.film.client.FilmProject;
import fr.ifsic.film.client.dto.PersonneDTO;

public class NouvellePersonne extends NouveauType {

    ListBox sexe;

    TextBox nom, prenom;

    public NouvellePersonne() {
        super("Entrez une personne");
        Grid grid = new Grid(3, 2);
        verticalPanel.add(grid);
        Label labelNom = new Label("Nom");
        grid.setWidget(0, 0, labelNom);
        nom = new TextBox();
        grid.setWidget(0, 1, nom);
        Label labelPrenom = new Label("Prenom");
        grid.setWidget(1, 0, labelPrenom);
        prenom = new TextBox();
        grid.setWidget(1, 1, prenom);
        Label labelSexe = new Label("Sexe");
        grid.setWidget(2, 0, labelSexe);
        sexe = new ListBox();
        grid.setWidget(2, 1, sexe);
        sexe.addItem("F");
        sexe.addItem("M");
        horizontalPanel.add(confirmer);
        horizontalPanel.add(annuler);
    }

    protected void confirmer() {
        FilmProject.changePage(FilmProject.accueil);
        PersonneDTO pdto = new PersonneDTO(nom.getText(), prenom.getText(), "M");
        FilmProject.greetingService.addP(pdto, new AsyncCallback<Integer>() {

            @Override
            public void onFailure(Throwable caught) {
            }

            @Override
            public void onSuccess(Integer result) {
            }
        });
        super.confirmer();
    }
}
