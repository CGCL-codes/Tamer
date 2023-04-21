package fr.ifsic.film.client.ui;

import com.google.gwt.user.client.rpc.AsyncCallback;
import fr.ifsic.film.client.FilmProject;
import fr.ifsic.film.client.dto.VedetteDTO;

public class ListeVedettes extends ListeType {

    VedetteDTO[] vdto;

    public ListeVedettes() {
        super("Liste des vedettes");
    }

    @Override
    void effacer() {
        FilmProject.greetingService.deleteV(vdto[listBox.getSelectedIndex()].getId(), new AsyncCallback<Void>() {

            @Override
            public void onFailure(Throwable caught) {
            }

            @Override
            public void onSuccess(Void result) {
            }
        });
        FilmProject.changePage(FilmProject.accueil);
    }

    @Override
    void details() {
        VedetteDTO v = vdto[listBox.getSelectedIndex()];
        if (v != null) {
            FilmProject.changePage(FilmProject.dv);
            ((DetailsVedette) FilmProject.dv).labelShowTitre.setText(v.getFilm().getTitre());
            ((DetailsVedette) FilmProject.dv).labelShowRealisateur.setText(v.getFilm().getPersonne().getNom() + " " + v.getFilm().getPersonne().getPrenom());
            ((DetailsVedette) FilmProject.dv).labelShowAnnee.setText(v.getFilm().getAnnee());
            ((DetailsVedette) FilmProject.dv).labelShowNom.setText(v.getPersonne().getNom());
            ((DetailsVedette) FilmProject.dv).labelShowPrenom.setText(v.getPersonne().getPrenom());
            ((DetailsVedette) FilmProject.dv).labelShowRole.setText(v.getRole());
        }
    }

    @Override
    void refresh() {
        FilmProject.greetingService.listVedette(new AsyncCallback<VedetteDTO[]>() {

            @Override
            public void onFailure(Throwable caught) {
            }

            @Override
            public void onSuccess(VedetteDTO[] result) {
                while (listBox.getItemCount() > 0) listBox.removeItem(0);
                vdto = result;
                for (VedetteDTO v : vdto) {
                    listBox.addItem(v.getPersonne().getNom() + "(" + v.getFilm().getTitre() + ")");
                }
            }
        });
    }
}
