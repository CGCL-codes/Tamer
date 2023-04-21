package com.geproman.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import com.geproman.dao.BaseDao;
import com.geproman.model.Arq_Evento;
import com.geproman.model.Calendario;
import com.geproman.model.Categoria;
import com.geproman.model.Data;
import com.geproman.model.Evento;
import com.geproman.model.Gasto;
import com.geproman.model.Grupo;
import com.geproman.model.Grupo_Usu;
import com.geproman.model.Usuario;
import com.ibm.faces.component.html.HtmlFileupload;
import com.ibm.faces.fileupload.util.ContentElement;

public class UsuarioController {

    private Usuario usuario;

    private Categoria categoria;

    private Grupo_Usu grupo_usu;

    private Grupo grupo;

    private Calendario calendario;

    private Evento evento;

    private Gasto gasto;

    private Data data;

    private Arq_Evento arquivo;

    private DataModel model;

    private String login;

    private String senha;

    private HtmlFileupload myFile;

    private String fusu;

    private String feven;

    private String fcat;

    private String fgru;

    public String getFcat() {
        return fcat;
    }

    public void setFcat(String fcat) {
        this.fcat = fcat;
    }

    public String getFeven() {
        return feven;
    }

    public void setFeven(String feven) {
        this.feven = feven;
    }

    public String getFgru() {
        return fgru;
    }

    public void setFgru(String fgru) {
        this.fgru = fgru;
    }

    public String getFusu() {
        return fusu;
    }

    public void setFusu(String fusu) {
        this.fusu = fusu;
    }

    public Arq_Evento getArquivo() {
        return arquivo;
    }

    public void setArquivo(Arq_Evento arquivo) {
        this.arquivo = arquivo;
    }

    public HtmlFileupload getMyFile() {
        return myFile;
    }

    public void setMyFile(HtmlFileupload myFile) {
        this.myFile = myFile;
    }

    public Gasto getGasto() {
        return gasto;
    }

    public void setGasto(Gasto gasto) {
        this.gasto = gasto;
    }

    public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public Calendario getCalendario() {
        return calendario;
    }

    public void setCalendario(Calendario calendario) {
        this.calendario = calendario;
    }

    public Grupo getGrupo() {
        return grupo;
    }

    public void setGrupo(Grupo grupo) {
        this.grupo = grupo;
    }

    public Grupo_Usu getGrupo_usu() {
        return grupo_usu;
    }

    public void setGrupo_usu(Grupo_Usu grupo_usu) {
        this.grupo_usu = grupo_usu;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public Usuario getUsuFromEditOrDelete() {
        Usuario usuario = (Usuario) model.getRowData();
        return usuario;
    }

    public Grupo_Usu getGrupoUsuFromDelete() {
        Grupo_Usu grupo_usu = (Grupo_Usu) model.getRowData();
        return grupo_usu;
    }

    public String novoUsuario() {
        this.usuario = new Usuario();
        return "novo";
    }

    public String login() {
        BaseDao<Usuario> dao = new BaseDao<Usuario>();
        Usuario usuario = dao.loginUsu(login, senha);
        setUsuario(usuario);
        if (usuario == null || usuario.isPerm() == false) return "falha_log"; else if (usuario.getLogin().toString().compareTo("admin") == 0) {
            return "login_admin";
        } else {
            System.out.print(usuario.getLogin());
            return "login_success";
        }
    }

    public String logout() {
        usuario = null;
        return "logout";
    }

    public String create() {
        BaseDao<Usuario> dao = new BaseDao<Usuario>();
        dao.create(usuario);
        BaseDao<Calendario> daoc = new BaseDao<Calendario>();
        this.calendario = new Calendario();
        calendario.setUsuario(usuario);
        daoc.create(calendario);
        return "success_creUsu";
    }

    public String delete() {
        BaseDao<Usuario> dao = new BaseDao<Usuario>();
        Usuario usuario = getUsuFromEditOrDelete();
        dao.delete(usuario);
        return "success_delUsu";
    }

    public String edit() {
        Usuario usuario = getUsuFromEditOrDelete();
        setUsuario(usuario);
        return "editUsu";
    }

    public String update() {
        BaseDao<Usuario> dao = new BaseDao<Usuario>();
        dao.update(usuario);
        return "success_upd";
    }

    public String perm() {
        Usuario usuario = getUsuFromEditOrDelete();
        usuario.setPerm(true);
        BaseDao<Usuario> dao = new BaseDao<Usuario>();
        dao.update(usuario);
        return "success_perm";
    }

    public String nPerm() {
        Usuario usuario = getUsuFromEditOrDelete();
        usuario.setPerm(false);
        BaseDao<Usuario> dao = new BaseDao<Usuario>();
        dao.update(usuario);
        return "success_nperm";
    }

    public DataModel getAllUsu() {
        BaseDao<Usuario> dao = new BaseDao<Usuario>();
        model = new ListDataModel(dao.allUsuario());
        return model;
    }

    private String keysearch;

    public String getKeysearch() {
        return keysearch;
    }

    public void setKeysearch(String keysearch) {
        this.keysearch = keysearch;
    }

    public DataModel getGru_UsuList() {
        BaseDao<Usuario> dao = new BaseDao<Usuario>();
        model = new ListDataModel(dao.gru_UsuByCodUsu(usuario.getCod_usu()));
        return model;
    }

    public String selectGru_Usu() {
        Grupo_Usu grupo_usu = getGrupoUsuFromDelete();
        setGrupo_usu(grupo_usu);
        return "list_cat";
    }

    public String selectUsus() {
        Grupo_Usu grupo_usu = getGrupoUsuFromDelete();
        setGrupo_usu(grupo_usu);
        return "list_grusu";
    }

    public DataModel getGru_UsuListByGrupo() {
        BaseDao<Grupo_Usu> dao = new BaseDao<Grupo_Usu>();
        model = new ListDataModel(dao.gru_UsuList(grupo_usu.getGrupo()));
        return model;
    }

    public DataModel getCatList() {
        BaseDao<Categoria> dao = new BaseDao<Categoria>();
        BaseDao<Grupo> daog = new BaseDao<Grupo>();
        Grupo grupo = daog.findGruByCod_Gru(grupo_usu.getCod_grupo());
        setGrupo(grupo);
        model = new ListDataModel(dao.catByCodGru(grupo));
        return model;
    }

    public DataModel getDataCal() {
        BaseDao<Calendario> daoc = new BaseDao<Calendario>();
        BaseDao<Data> daod = new BaseDao<Data>();
        Calendario calendario = daoc.findCalByUsu(usuario);
        setCalendario(calendario);
        model = new ListDataModel(daod.dataListByCal(calendario));
        return model;
    }

    public String novaData() {
        this.data = new Data();
        return "novaData";
    }

    public String novaDataAdmin() {
        this.data = new Data();
        return "novaData";
    }

    public String createData() {
        BaseDao<Data> dao = new BaseDao<Data>();
        data.setCalendario(calendario);
        dao.create(data);
        return "success_creData";
    }

    public String deleteData() {
        BaseDao<Data> dao = new BaseDao<Data>();
        Data data = (Data) model.getRowData();
        dao.delete(data);
        return "success_delData";
    }

    public String editData() {
        Data data = (Data) model.getRowData();
        setData(data);
        return "editData";
    }

    public String updateData() {
        BaseDao<Data> dao = new BaseDao<Data>();
        dao.update(data);
        return "success_updData";
    }

    public String selectEvens() {
        Categoria categoria = (Categoria) model.getRowData();
        setCategoria(categoria);
        return "listEven";
    }

    public DataModel getEvensCat() {
        BaseDao<Evento> dao = new BaseDao<Evento>();
        model = new ListDataModel(dao.evenListByCat(categoria));
        return model;
    }

    public String selectGastos() {
        Evento evento = (Evento) model.getRowData();
        setEvento(evento);
        return "listGasto";
    }

    public DataModel getGastoEven() {
        BaseDao<Gasto> dao = new BaseDao<Gasto>();
        model = new ListDataModel(dao.gasListByEven(evento));
        return model;
    }

    public String novoGasto() {
        this.gasto = new Gasto();
        return "novoGasto";
    }

    public String createGasto() {
        BaseDao<Gasto> dao = new BaseDao<Gasto>();
        gasto.setEvento(evento);
        dao.create(gasto);
        return "success_creGasto";
    }

    public String listArq() {
        Evento evento = (Evento) model.getRowData();
        setEvento(evento);
        BaseDao<Arq_Evento> dao = new BaseDao<Arq_Evento>();
        setArquivo(dao.arqByEven(getEvento()));
        return "listArq";
    }

    public synchronized String uploadMyFile() throws IOException {
        HtmlFileupload file = getMyFile();
        ContentElement content = (ContentElement) file.getValue();
        String pasta = "C:/upload/";
        if (content != null) {
            File arq = new File(pasta + evento.getTitulo() + ".zip");
            FileOutputStream item = new FileOutputStream(arq);
            item.write(content.getContentValue());
            BaseDao<Arq_Evento> dao = new BaseDao<Arq_Evento>();
            arquivo.setArq(pasta + evento.getTitulo() + ".zip");
            Date now = new Date();
            arquivo.setData(now);
            dao.update(arquivo);
        } else {
            System.out.print("o arquivo esta vazio");
        }
        return "okUpload";
    }

    public DataModel getFindUsu() {
        BaseDao<Usuario> dao = new BaseDao<Usuario>();
        model = new ListDataModel(dao.findUsus(getFusu()));
        return model;
    }

    public DataModel getFindEven() {
        BaseDao<Evento> dao = new BaseDao<Evento>();
        model = new ListDataModel(dao.findEven(getFeven()));
        return model;
    }

    public DataModel getFindCat() {
        BaseDao<Categoria> dao = new BaseDao<Categoria>();
        model = new ListDataModel(dao.findCat(getFcat()));
        return model;
    }

    public DataModel getFindGru() {
        BaseDao<Grupo> dao = new BaseDao<Grupo>();
        model = new ListDataModel(dao.findGru(getFgru()));
        return model;
    }
}
