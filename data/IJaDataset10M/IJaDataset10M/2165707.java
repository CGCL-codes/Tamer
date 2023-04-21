package br.unioeste.modelo;

import java.util.ArrayList;

/**
 *
 * @author Moises
 */
public class FitoP {

    Filo filo;

    Classe classe;

    Ordem ordem;

    Genero genero;

    Familia familia;

    Especie especie;

    Planta planta;

    PPlanta pplanta;

    String nome_fp;

    boolean disponivel;

    Usuario usuario;

    ArrayList<Integer> fpfoto;

    public FitoP(Filo filo, Classe classe, Ordem ordem, Genero genero, Familia familia, Especie especie, Planta planta, PPlanta pplanta, String nome_fp, boolean disponivel, Usuario usuario, ArrayList<Integer> fpfoto) {
        this.filo = filo;
        this.classe = classe;
        this.ordem = ordem;
        this.genero = genero;
        this.familia = familia;
        this.especie = especie;
        this.planta = planta;
        this.pplanta = pplanta;
        this.nome_fp = nome_fp;
        this.disponivel = disponivel;
        this.usuario = usuario;
        this.fpfoto = fpfoto;
    }

    public FitoP() {
        this.filo = new Filo();
        this.classe = new Classe();
        this.ordem = new Ordem();
        this.genero = new Genero();
        this.familia = new Familia();
        this.especie = new Especie();
        this.planta = new Planta();
        this.pplanta = new PPlanta();
        this.nome_fp = new String();
        this.disponivel = false;
        this.usuario = new Usuario();
        this.fpfoto = new ArrayList();
    }

    public Classe getClasse() {
        return classe;
    }

    public void setClasse(Classe classe) {
        this.classe = classe;
    }

    public boolean isDisponivel() {
        return disponivel;
    }

    public void setDisponivel(boolean disponivel) {
        this.disponivel = disponivel;
    }

    public Especie getEspecie() {
        return especie;
    }

    public void setEspecie(Especie especie) {
        this.especie = especie;
    }

    public Familia getFamilia() {
        return familia;
    }

    public void setFamilia(Familia familia) {
        this.familia = familia;
    }

    public Filo getFilo() {
        return filo;
    }

    public void setFilo(Filo filo) {
        this.filo = filo;
    }

    public ArrayList<Integer> getFpfoto() {
        return fpfoto;
    }

    public void setFpfoto(ArrayList<Integer> fpfoto) {
        this.fpfoto = fpfoto;
    }

    public Genero getGenero() {
        return genero;
    }

    public void setGenero(Genero genero) {
        this.genero = genero;
    }

    public String getNome_fp() {
        return nome_fp;
    }

    public void setNome_fp(String nome_fp) {
        this.nome_fp = nome_fp;
    }

    public Ordem getOrdem() {
        return ordem;
    }

    public void setOrdem(Ordem ordem) {
        this.ordem = ordem;
    }

    public Planta getPlanta() {
        return planta;
    }

    public void setPlanta(Planta planta) {
        this.planta = planta;
    }

    public PPlanta getPplanta() {
        return pplanta;
    }

    public void setPplanta(PPlanta pplanta) {
        this.pplanta = pplanta;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
