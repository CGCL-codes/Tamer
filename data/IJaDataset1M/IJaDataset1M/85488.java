package com.unice.miage.oobdoo.manager;

import com.unice.miage.oobdoo.entity.Album;
import com.unice.miage.oobdoo.entity.Commentaire;
import com.unice.miage.oobdoo.entity.Note;
import com.unice.miage.oobdoo.entity.Partage;
import com.unice.miage.oobdoo.entity.Photo;
import com.unice.miage.oobdoo.entity.Utilisateur;
import com.unice.miage.oobdoo.enumeration.Statut;
import java.io.File;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Jeremy Romano
 */
@Stateless
public class UtilisateurManager {

    @PersistenceContext
    private EntityManager em;

    @EJB
    private AlbumManager albumManager;

    @EJB
    private PhotoManager photoManager;

    @EJB
    private CommentaireManager commentaireManager;

    @EJB
    private PartageManager partageManager;

    private String c;

    public void setPATH(String c) {
        this.c = c;
    }

    public String getPATH() {
        return this.c;
    }

    /**
     * Creation d'un utilisateur
     * @param login - le login
     * @param password - le mot de passe
     * @param email - l'email
     * @return l'utilisateur cree
     */
    public Utilisateur creeUtilisateur(String login, String password, String email) {
        Utilisateur u = new Utilisateur(login, password, email);
        em.persist(u);
        String strDirectory = this.getPATH() + "upload/" + u.getId();
        try {
            (new File(strDirectory)).mkdir();
        } catch (Exception ex) {
        }
        return u;
    }

    public void creePremiersUtilisateurs() {
        Utilisateur remi = creeUtilisateur("xenogaz", "qsdqsd", "remi.heens@gmail.com");
        albumManager.setPATH(this.getPATH());
        Album velo = albumManager.creerAlbum(remi, "Velo", "Plein de photos de velo extreme !", Statut.PUBLIC);
        Album vacances2010 = albumManager.creerAlbum(remi, "Vacances 2010", "Mes vacances 2010", Statut.PRIVATE);
        addAlbum(remi, velo);
        addAlbum(remi, vacances2010);
        Utilisateur jey = creeUtilisateur("jeremy", "azerty", "romano.jeremy@gmail.com");
        Album test = albumManager.creerAlbum(jey, "Test", "Mon premier album de test", Statut.PRIVATE);
        Album escalade = albumManager.creerAlbum(jey, "Escalade", "Escalade à gogo!", Statut.PRIVATE);
        Album images = albumManager.creerAlbum(jey, "Images", "De tout et de rien...", Statut.PUBLIC);
        Photo hello = photoManager.creerPhoto(test, "Hello World!", "Premiere image de test", "AcidTest2.png", "AcidTest2_thumb_224.png", "AcidTest2_thumb_140.png", "AcidTest2_thumb_500.png");
        Photo doctorWho = photoManager.creerPhoto(test, "Doctor Who", "", "Doctor_Who__s_OTP_by_nuriwan.jpg", "Doctor_Who__s_OTP_by_nuriwan_thumb_224.jpg", "Doctor_Who__s_OTP_by_nuriwan_thumb_140.jpg", "Doctor_Who__s_OTP_by_nuriwan_thumb_500.jpg");
        Photo nationalGeo = photoManager.creerPhoto(escalade, "National Geographic", "", "climb1.jpg", "climb1_thumb_224.jpg", "climb1_thumb_140.jpg", "climb1_thumb_500.jpg");
        Photo mer = photoManager.creerPhoto(escalade, "Mer", "", "climb2.jpg", "climb2_thumb_224.jpg", "climb2_thumb_140.jpg", "climb2_thumb_500.jpg");
        Photo glace = photoManager.creerPhoto(escalade, "Glace", "", "climb3.jpg", "climb3_thumb_224.jpg", "climb3_thumb_140.jpg", "climb3_thumb_500.jpg");
        Commentaire wawe = commentaireManager.creerCommentaire(jey, doctorWho, "Wawe mais c'est trop beau !!!");
        try {
            Thread.sleep(1);
        } catch (InterruptedException ex) {
            Logger.getLogger(UtilisateurManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        Commentaire encore = commentaireManager.creerCommentaire(jey, doctorWho, "Encore encore encore!");
        try {
            Thread.sleep(1);
        } catch (InterruptedException ex) {
            Logger.getLogger(UtilisateurManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        Commentaire ahah = commentaireManager.creerCommentaire(jey, hello, "ahah hello !!!!");
        try {
            Thread.sleep(1);
        } catch (InterruptedException ex) {
            Logger.getLogger(UtilisateurManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        Commentaire trop = commentaireManager.creerCommentaire(remi, glace, "trop fort!!");
        try {
            Thread.sleep(1);
        } catch (InterruptedException ex) {
            Logger.getLogger(UtilisateurManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        Commentaire coolTop = commentaireManager.creerCommentaire(remi, mer, "C'est trop cool ça !! J'aimerai trop être à la place de la personne !!!! :)");
        try {
            Thread.sleep(1);
        } catch (InterruptedException ex) {
            Logger.getLogger(UtilisateurManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        Commentaire cafaitplaisir = commentaireManager.creerCommentaire(remi, doctorWho, "ca fait plaisir!");
        try {
            Thread.sleep(1);
        } catch (InterruptedException ex) {
            Logger.getLogger(UtilisateurManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        Commentaire blabla = commentaireManager.creerCommentaire(remi, hello, "blabla");
        try {
            Thread.sleep(1);
        } catch (InterruptedException ex) {
            Logger.getLogger(UtilisateurManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        Commentaire yo = commentaireManager.creerCommentaire(remi, hello, "Yo !");
        try {
            Thread.sleep(1);
        } catch (InterruptedException ex) {
            Logger.getLogger(UtilisateurManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        Commentaire naz = commentaireManager.creerCommentaire(remi, doctorWho, "Pouf !\n Elle est naze ta photo...\n\n Trouve autre chose la prochaine fois!");
        try {
            Thread.sleep(1);
        } catch (InterruptedException ex) {
            Logger.getLogger(UtilisateurManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        photoManager.addCommentaire(hello, ahah);
        photoManager.addCommentaire(doctorWho, wawe);
        photoManager.addCommentaire(doctorWho, encore);
        photoManager.addCommentaire(glace, trop);
        photoManager.addCommentaire(mer, coolTop);
        photoManager.addCommentaire(doctorWho, cafaitplaisir);
        photoManager.addCommentaire(hello, blabla);
        photoManager.addCommentaire(hello, yo);
        photoManager.addCommentaire(doctorWho, naz);
        addCommentaire(remi, coolTop);
        addCommentaire(remi, trop);
        addCommentaire(jey, wawe);
        addCommentaire(jey, encore);
        addCommentaire(jey, ahah);
        albumManager.addPhoto(test, hello);
        albumManager.addPhoto(test, doctorWho);
        albumManager.addPhoto(escalade, nationalGeo);
        albumManager.addPhoto(escalade, mer);
        albumManager.addPhoto(escalade, glace);
        addAlbum(jey, test);
        addAlbum(jey, escalade);
        addAlbum(jey, images);
        Utilisateur buffa = creeUtilisateur("buffa", "azerty", "buffa@enseignant.com");
        Partage buffaVelo = partageManager.creerPartage(buffa, velo);
        albumManager.addPartage(velo, buffaVelo);
        velo = albumManager.getAlbumById(velo.getId());
        addPartage(buffa, buffaVelo);
        buffa = getUtilisateurById(buffa.getId());
        Partage jeyVelo = partageManager.creerPartage(jey, velo);
        albumManager.addPartage(velo, jeyVelo);
        velo = albumManager.getAlbumById(velo.getId());
        addPartage(jey, jeyVelo);
        jey = getUtilisateurById(jey.getId());
        Partage remiTest = partageManager.creerPartage(remi, test);
        albumManager.addPartage(test, remiTest);
        test = albumManager.getAlbumById(test.getId());
        addPartage(remi, remiTest);
        remi = getUtilisateurById(remi.getId());
        Partage remiEscalade = partageManager.creerPartage(remi, escalade);
        albumManager.addPartage(escalade, remiEscalade);
        escalade = albumManager.getAlbumById(escalade.getId());
        addPartage(remi, remiEscalade);
        remi = getUtilisateurById(remi.getId());
        Photo velo1 = photoManager.creerPhoto(velo, "Velo1", "", "06hpi0948.jpg", "06hpi0948_thumb_224.jpg", "06hpi0948_thumb_140.jpg", "06hpi0948_thumb_500.jpg");
        Photo velo2 = photoManager.creerPhoto(velo, "Velo2", "", "343885862_small.jpg", "343885862_small_thumb_224.jpg", "343885862_small_thumb_140.jpg", "343885862_small_thumb_500.jpg");
        albumManager.addPhoto(velo, velo1);
        albumManager.addPhoto(velo, velo2);
        Album cool = albumManager.creerAlbum(buffa, "Cool", "Un album qui fait plaisir", Statut.PRIVATE);
        addAlbum(buffa, cool);
    }

    /**
     * Pour savoir si un utilisateur existe
     * @param login - le login
     * @return {@code true} si l'utilisateur existe, {@code false} sinon.
     */
    public boolean isExist(String login) {
        try {
            Utilisateur u = this.getUtilisateurByLogin(login);
            return true;
        } catch (NoResultException ex) {
            return false;
        }
    }

    /**
     * Renvoie la liste des utilisateurs ayant un login approximatif
     * @param login - le login
     * @return la liste des utilisateurs
     */
    public Collection<Utilisateur> searchByLogin(String login) {
        Query q = em.createQuery("select u from Utilisateur u where lower(u.login) like :login");
        q.setParameter("login", "%" + login.toLowerCase() + "%");
        return q.getResultList();
    }

    /**
     * Renvoie la liste des utilisateurs ayant un login approximatif
     * @param s - le login
     * @param first - le premier resultat
     * @param max - le nombre max de resultat a afficher
     * @return la liste des utilisateurs
     */
    public Collection<Utilisateur> search(String s, int first, int max) {
        Query q = em.createQuery("select u from Utilisateur u where lower(u.login) like :s");
        q.setParameter("s", "%" + s.toLowerCase() + "%");
        q.setFirstResult(first);
        q.setMaxResults(max);
        return q.getResultList();
    }

    /**
     * Change les informations d'un utilisateur
     * @param login - le login de l'utilisateur
     * @param email - le nouveau email
     * @return l'utilisateur modifie
     */
    public Utilisateur updateUserByLogin(String login, String email) {
        Utilisateur u = this.getUtilisateurByLogin(login);
        u.setEmail(email);
        em.merge(u);
        return u;
    }

    /**
     * Change les informations d'un utilisateur
     * @param id - l'id de l'utilisateur
     * @param login - le login de l'utilisateur
     * @param email - le nouveau email
     * @return l'utilisateur modifie
     */
    public Utilisateur updateUserById(int id, String login, String email) {
        Utilisateur u = this.getUtilisateurById(id);
        u.setLogin(login);
        u.setEmail(email);
        em.merge(u);
        return u;
    }

    /**
     * Change les informations d'un utilisateur
     * @param id - l'id de l'utilisateur
     * @param login - le login de l'utilisateur
     * @param email - le nouveau email
     * @return l'utilisateur modifie
     */
    public Utilisateur updateUserById(int id, String login, String email, String pwd) {
        Utilisateur u = this.getUtilisateurById(id);
        u.setLogin(login);
        u.setEmail(email);
        u.setPassword(pwd);
        em.merge(u);
        return u;
    }

    /**
     * Renvoie un utilisateur en fonction de son login
     * @param login - le login
     * @return l'utilisateur ayant ce login
     */
    public Utilisateur getUtilisateurByLogin(String login) {
        Query q = em.createQuery("select u from Utilisateur u where lower(u.login) like :login");
        q.setParameter("login", login.toLowerCase());
        return (Utilisateur) q.getSingleResult();
    }

    /**
     * Renvoie un utilisateur en fonction de son id
     * @param id - l'id
     * @return l'utilisateur ayant cet id
     */
    public Utilisateur getUtilisateurById(int id) {
        return em.find(Utilisateur.class, id);
    }

    /**
     * Supprime un utilisateur en fonction du login
     * @param login - le login
     * @return l'utilisateur supprime
     */
    public Utilisateur deleteUtilisateurByLogin(String login) {
        Utilisateur u = this.getUtilisateurByLogin(login);
        em.remove(u);
        return u;
    }

    /**
     * Renvoie la liste de tous les utilisateurs
     * @return la liste de tous les utilisateurs
     */
    public Collection<Utilisateur> getAllUsers() {
        Query q = em.createQuery("select u from Utilisateur u");
        try {
            return q.getResultList();
        } catch (NoResultException ex) {
            return null;
        }
    }

    /**
     * Pour savoir si un utilisateur peut se connecter
     * @param login - le login
     * @param password - le mot de passe
     * @return {@code true} si l'utilisateur peut se connecter, {@code false} sinon
     */
    public boolean acceptConnexion(String login, String password) {
        try {
            Utilisateur u = this.getUtilisateurByLogin(login);
            if (u.isGoodPassword(password)) {
                return true;
            } else {
                return false;
            }
        } catch (NoResultException ex) {
            return false;
        }
    }

    /**
     * Pour savoir si un login existe
     * @param login - le login
     * @return {@code true} si le login existe, {@code false} sinon.
     */
    public boolean checkIfLoginExist(String login) {
        Query q = em.createQuery("select count(u) from Utilisateur as u where lower(u.login) = :s");
        q.setParameter("s", login.toLowerCase());
        int count = ((Long) q.getSingleResult()).intValue();
        if (count > 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Renvoie la liste de tous les utilisateurs
     * @param first - le premier resultat a retourner
     * @param max - le nombre max de resultat a retourner
     * @return la liste de tous les utilisateurs
     */
    public Collection<Utilisateur> getAllUsers(int first, int max) {
        Query q = em.createQuery("select u from Utilisateur u");
        q.setFirstResult(first);
        q.setMaxResults(max);
        return q.getResultList();
    }

    /**
     * Le nombre d'utilisateurs
     * @return Le nombre d'utilisateur
     */
    public double getGlobalCount() {
        Query q = em.createQuery("select count(o) from Utilisateur as o");
        int count = ((Long) q.getSingleResult()).intValue();
        return count;
    }

    /**
     * Le nombre d'utilateurs d'un login approximatif
     * @param s - le login
     * @return le nombre d'un login approximatif
     */
    public double getGlobalCount(String s) {
        Query q = em.createQuery("select count(o) from Utilisateur as o where lower(o.login) like :s ");
        q.setParameter("s", "%" + s.toLowerCase() + "%");
        int count = ((Long) q.getSingleResult()).intValue();
        return count;
    }

    /**
     * Supprime un utilisateur en fonction d'un id
     * @param id - l'id
     * @return l'utilisateur supprime
     */
    public Utilisateur deleteUtilisateurById(int id) {
        Utilisateur u = this.getUtilisateurById(id);
        em.remove(u);
        return u;
    }

    /**
     * Mise a jour de l'email d'un utilisateur
     * @param u - l'utilisateur a mettre a jour
     * @param email - le nouveau email
     * @return L'utilisateur mis a jour
     */
    public Utilisateur updateMail(Utilisateur u, String email) {
        u.setEmail(email);
        em.merge(u);
        return u;
    }

    public Utilisateur updateAccesTokenFB(Utilisateur u, String acces_token_fb) {
        u.setAccess_token_fb(acces_token_fb);
        em.merge(u);
        return u;
    }

    public Utilisateur updateAccesTokenPICASA(Utilisateur u, String acces_token_picasa) {
        u.setAccess_token_picasa(acces_token_picasa);
        em.merge(u);
        return u;
    }

    public Utilisateur updateAccesTokenFLICKR(Utilisateur u, String acces_token_flickr) {
        u.setAccess_token_flickr(acces_token_flickr);
        em.merge(u);
        return u;
    }

    /**
     * Mise a jour de la date de derniere connexion d'un utilisateur
     * @param u - l'utilisateur a mettre a jour
     * @param time - la nouvelle date
     * @return L'utilisateur mis a jour
     */
    public Utilisateur updateLastLogin(Utilisateur u, long time) {
        u.setLastLogin(time);
        em.merge(u);
        return u;
    }

    public void addAlbum(Utilisateur u, Album a) {
        u.addAlbum(a);
        em.merge(u);
    }

    public void addPartage(Utilisateur u, Partage p) {
        u.getPartages().add(p);
        em.merge(u);
    }

    public void deletePartage(Utilisateur u, Partage p) {
        Collection<Partage> partages = u.getPartages();
        for (Partage partage : partages) {
            if (partage.getId() == p.getId()) {
                partages.remove(partage);
                break;
            }
        }
        em.merge(u);
    }

    public void deleteNote(Utilisateur u, Note n) {
        Collection<Note> notes = u.getNotes();
        for (Note note : notes) {
            if (note.getId() == n.getId()) {
                notes.remove(note);
                break;
            }
        }
        em.merge(u);
    }

    public void deleteAlbum(Utilisateur u, Album a) {
        Collection<Album> albums = u.getAlbums();
        for (Album album : albums) {
            if (album.getId() == a.getId()) {
                albums.remove(album);
                break;
            }
        }
        em.merge(u);
    }

    public void addCommentaire(Utilisateur u, Commentaire c) {
        u.addCommentaire(c);
        em.merge(u);
    }

    public Utilisateur getUtilisateurByUUID(String uuid, String login) {
        try {
            Query q = em.createQuery("select u from Utilisateur u where lower(u.login) = :login and u.uniquekey = :uk");
            q.setParameter("uk", uuid);
            q.setParameter("login", login.toLowerCase());
            return (Utilisateur) q.getSingleResult();
        } catch (Exception ex) {
            return null;
        }
    }

    public void deleteCommentaire(Utilisateur u, Commentaire c) {
        Collection<Commentaire> commentaires = u.getCommentaires();
        for (Commentaire com : commentaires) {
            if (com.getId() == c.getId()) {
                commentaires.remove(com);
                break;
            }
        }
        em.merge(u);
    }
}
