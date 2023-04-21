package fr.ifsic.film.client.ui;

import com.google.gwt.user.client.rpc.AsyncCallback;
import fr.ifsic.film.client.FilmProject;
import fr.ifsic.film.client.dto.FilmDTO;

public class ListeFilms extends ListeType {

    FilmDTO[] films;

    public ListeFilms() {
        super("Liste des films");
    }

    @Override
    void effacer() {
        FilmProject.greetingService.deleteF(films[listBox.getSelectedIndex()].getF(), new AsyncCallback<Void>() {

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
        FilmDTO f = films[listBox.getSelectedIndex()];
        if (f != null) {
            FilmProject.changePage(FilmProject.df);
            ((DetailsFilm) FilmProject.df).labelShowId.setText(f.getF() + "");
            ((DetailsFilm) FilmProject.df).labelShowTitre.setText(f.getTitre());
            ((DetailsFilm) FilmProject.df).labelShowAnnee.setText(f.getAnnee());
            ((DetailsFilm) FilmProject.df).labelShowGenre.setText(f.getGenre());
            ((DetailsFilm) FilmProject.df).labelShowDuree.setText(f.getDuree());
            ((DetailsFilm) FilmProject.df).labelShowRealisateur.setText(f.getPersonne().getNom() + " " + f.getPersonne().getPrenom());
        }
    }

    @Override
    void refresh() {
        FilmProject.greetingService.listFilm(new AsyncCallback<FilmDTO[]>() {

            @Override
            public void onFailure(Throwable caught) {
            }

            @Override
            public void onSuccess(FilmDTO[] result) {
                while (listBox.getItemCount() > 0) listBox.removeItem(0);
                films = result;
                for (FilmDTO f : films) {
                    listBox.addItem(f.getTitre());
                }
            }
        });
    }
}
