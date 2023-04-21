package isg3.article;

import isg3.article.*;
import isg3.user.*;
import java.util.*;

public class Wiki {

    private List listArt;

    private List listCat;

    private List listUser;

    private static Wiki wiki;

    private Wiki() {
        listArt = new ArrayList();
        listCat = new ArrayList();
        listUser = new ArrayList();
        User u1 = new User("eagleXP", "pass1");
        User u2 = new User("flipaovyse", "pass2");
        User u3 = new User("practica", "practica");
        User u4 = new User("Lopera", "hugo");
        Category c1 = new Category("Deportes", "Categoria de Deportes");
        Category c2 = new Category("Ciencias", "Categoria de Ciencias");
        Category c3 = new Category("Sociedad", "Categoria de Sociedad");
        Article a1 = new ArticleFactory().createArticle("Sevilla FC", "El mejor equipo del mundo casi se mete en champions", c1, u1);
        a1.getRates().addNewRate(new Rate(0, "Soy Lopera y el Sevilla es una caca", u4));
        listUser.add(u1);
        listUser.add(u2);
        listUser.add(u3);
        listCat.add(c1);
        listCat.add(c2);
        listCat.add(c3);
        listArt.add(a1);
        listArt.add(new ArticleFactory().createArticle("Einstein", "El alumno de informatica con nick flipaovyse tiene el coeficiente de Einstein", c2, u2));
        listArt.add(new ArticleFactory().createArticle("Fiesta politica", "Zapatero se va de copas con Rajoy y su niña", c3, u3));
    }

    public static synchronized Wiki getInstance() {
        if (wiki == null) {
            wiki = new Wiki();
        }
        return wiki;
    }

    public Collection getListArt() {
        return listArt;
    }

    public Collection getListCat() {
        return listCat;
    }

    public Collection getListUser() {
        return listUser;
    }
}
