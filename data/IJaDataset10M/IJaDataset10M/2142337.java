package Metier.Requete;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import Controleur.ControleurGestionRelation;
import DAO.RequeteDAO.RequeteDAO;
import Factory.Factory;
import Metier.Contrainte;

public class Requete {

    private List<TableRequete> listeTableRequete;

    private RequeteDAO dao;

    private String join;

    private String select;

    private List<String> listeFrom;

    private List<String> listeJoin;

    private List<String> listeJoinAttente;

    private List<String> listeJoinAttenteNom;

    private String where;

    public Requete() {
        dao = Factory.getRequeteDAO();
        this.listeTableRequete = new ArrayList<TableRequete>();
    }

    public void addTableRequete(String select, String from, String where) {
        ControleurGestionRelation controleurGR = ControleurGestionRelation.getInstance();
        int id = getIndex(from);
        TableRequete tr;
        if (id == -1) tr = new TableRequete(select, from, where, controleurGR.getIndexAllTable(from), true); else tr = new TableRequete(select, from, where, controleurGR.getIndexAllTable(from), false);
        this.listeTableRequete.add(tr);
    }

    public ResultatRequete executer() throws SQLException {
        ResultatRequete rr = null;
        getSelect();
        if (this.listeFrom == null) getFrom();
        getWhere();
        String from = getListFrom();
        if ((!select.equals("")) && (!from.equals(""))) rr = dao.executer(select, from, join, where);
        return rr;
    }

    private String getListFrom() {
        String from = "";
        ControleurGestionRelation controleurGR = ControleurGestionRelation.getInstance();
        for (int i = 0; i < this.listeFrom.size(); i++) {
            if (!from.equals("")) from += ",";
            from += this.listeFrom.get(i) + " t" + controleurGR.getIndexAllTable(listeFrom.get(i));
        }
        return from;
    }

    private void getSelect() {
        select = "";
        for (int i = 0; i < this.listeTableRequete.size(); i++) {
            if (!select.equals("")) select += ",";
            select += this.listeTableRequete.get(i).getSelect();
        }
    }

    private void getFrom() {
        listeFrom = new ArrayList<String>();
        for (int i = 0; i < this.listeTableRequete.size(); i++) {
            if (this.listeTableRequete.get(i).getEtatFrom()) {
                listeFrom.add(this.listeTableRequete.get(i).getFrom());
            }
        }
    }

    private void getWhere() {
        where = "";
        for (int i = 0; i < this.listeTableRequete.size(); i++) {
            if (!this.listeTableRequete.get(i).getWhere().equals("")) {
                if (!where.equals("")) where += ",";
                where += this.listeTableRequete.get(i).getWhere();
            }
        }
    }

    private int getIndex(String nomTable) {
        int i = 0;
        if (this.listeTableRequete.size() != 0) {
            while ((i < this.listeTableRequete.size() && (!this.listeTableRequete.get(i).getNomTable().equals(nomTable)))) {
                i++;
            }
            if (i == this.listeTableRequete.size()) i = -1;
        } else i = -1;
        return i;
    }

    public void addContrainte(Contrainte contrainte) {
        System.out.println("add C");
        String Tcourante = "";
        boolean ajouter = true;
        if (listeJoin == null) listeJoin = new ArrayList<String>();
        if (listeJoinAttente == null) this.listeJoinAttente = new ArrayList<String>();
        if (listeJoinAttenteNom == null) this.listeJoinAttenteNom = new ArrayList<String>();
        if (this.listeFrom == null) getFrom();
        ControleurGestionRelation controleurGR = ControleurGestionRelation.getInstance();
        int index1 = controleurGR.getIndexAllTable(contrainte.getNomTable1());
        int index2 = controleurGR.getIndexAllTable(contrainte.getNomTable2());
        System.out.println("index" + contrainte.getNomTable1() + ":" + this.listeFrom.indexOf(contrainte.getNomTable1()));
        if (this.listeFrom.indexOf(contrainte.getNomTable1()) != -1) {
            if (this.listeFrom.indexOf(contrainte.getNomTable2()) != -1) {
                if (this.listeJoin.indexOf(contrainte.getNomTable1()) == -1) {
                    int index = this.listeFrom.indexOf(contrainte.getNomTable1());
                    if (index != -1) this.listeFrom.remove(index);
                    if (join == null) join = " JOIN "; else join += "\nJOIN ";
                    join += contrainte.getNomTable1() + " t" + index1;
                    Tcourante = contrainte.getNomTable1();
                    System.out.println(Tcourante + " : 1");
                } else System.out.println("doublon1 :" + contrainte.getNomTable1());
            } else {
                if (this.listeJoin.indexOf(contrainte.getNomTable2()) == -1) {
                    if (join == null) join = " JOIN "; else join += "\nJOIN ";
                    join += contrainte.getNomTable2() + " t" + index2;
                    Tcourante = contrainte.getNomTable2();
                    System.out.println(Tcourante + " : 2");
                } else {
                    System.out.println("doublon2 :" + contrainte.getNomTable2());
                    this.listeFrom.remove(this.listeFrom.indexOf(contrainte.getNomTable1()));
                    if (join == null) join = " JOIN "; else join += "\nJOIN ";
                    join += contrainte.getNomTable1() + " t" + index1;
                    Tcourante = contrainte.getNomTable1();
                    System.out.println(Tcourante + " : 3");
                }
            }
        } else {
            if (this.listeJoin.indexOf(contrainte.getNomTable1()) == -1) {
                if (join == null) join = " JOIN "; else join += "\nJOIN ";
                join += contrainte.getNomTable1() + " t" + index1;
                Tcourante = contrainte.getNomTable1();
                System.out.println(Tcourante + " : 4");
            } else System.out.println("doublon1 :" + contrainte.getNomTable1());
        }
        if (ajouter) {
            join += " ON ";
            for (int i = 0; i < contrainte.getListCAttr1().size(); i++) {
                if (i != 0) join += " and ";
                join += "t" + index1 + "." + contrainte.getListCAttr1().get(i) + " = ";
                join += "t" + index2 + "." + contrainte.getListCAttr2().get(i);
            }
            this.listeJoin.add(Tcourante);
            int index = this.listeJoinAttenteNom.indexOf(Tcourante);
            if (index != -1) {
                this.join += " " + this.listeJoinAttente.get(index);
                this.listeJoinAttente.remove(index);
                this.listeJoinAttenteNom.remove(index);
            }
        }
    }
}
