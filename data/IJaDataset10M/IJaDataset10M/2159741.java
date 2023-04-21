package com.projeto.beans;

import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import org.richfaces.event.UploadEvent;
import org.richfaces.model.UploadItem;
import com.projeto.persistencia.dao.DAOMidia;
import com.projeto.persistencia.dao.DAOTag;
import com.projeto.persistencia.entidades.Midia;
import com.projeto.persistencia.entidades.Tag;
import com.projeto.util.Converter;
import com.projeto.util.Encription;

public class CadastrarBean {

    public CadastrarBean() {
    }

    private Midia midia;

    private String tagsDeEntrada = "";

    public Midia getMidia() {
        return midia;
    }

    public void setMidia(Midia midia) {
        this.midia = midia;
    }

    public String getTagsDeEntrada() {
        return tagsDeEntrada;
    }

    public void setTagsDeEntrada(String tagsDeEntrada) {
        this.tagsDeEntrada = tagsDeEntrada;
    }

    private void cadastraTags() {
        StringTokenizer tokens = new StringTokenizer(tagsDeEntrada, ";");
        DAOTag dao = new DAOTag();
        while (tokens.hasMoreElements()) {
            Tag t = new Tag(tokens.nextToken());
            List<Tag> res = dao.findByField("nome", t.getNome());
            if (!res.isEmpty()) {
                t = res.get(0);
            }
            midia.getTags().add(t);
            t.getMidias().add(this.midia);
        }
        dao.close();
    }

    public void upload(UploadEvent event) {
        this.midia = new Midia();
        try {
            FacesContext fc = FacesContext.getCurrentInstance();
            ServletContext ctx = (ServletContext) fc.getExternalContext().getContext();
            UploadItem item = event.getUploadItem();
            String filename = Encription.hashMD5(item.getFileName() + new Date().getTime());
            String path = ctx.getRealPath("/arquivos/") + "\\";
            if (item.getFileName().endsWith("avi") || item.getFileName().endsWith("wmv") || item.getFileName().endsWith("flv") || item.getFileName().endsWith("mpg")) {
                this.midia.setTipo("V");
                Converter con = new Converter();
                con.Convert(item.getFile().getPath(), path + filename + ".flv");
                this.midia.setCaminho(filename + ".flv");
            } else {
                if (item.getFileName().endsWith("mp3") || item.getFileName().endsWith("wma") || item.getFileName().endsWith("wav")) {
                    this.midia.setTipo("M");
                    Converter con = new Converter();
                    con.setOptions("-ab 128k");
                    con.Convert(item.getFile().getPath(), path + filename + ".mp3");
                    this.midia.setCaminho(filename + ".mp3");
                } else System.out.println("Formato de Arquivo Inv�lido");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String gravar() {
        DAOMidia daoMidia = new DAOMidia();
        cadastraTags();
        daoMidia.begin();
        daoMidia.persist(midia);
        daoMidia.commit();
        daoMidia.close();
        return "/index.xhtml";
    }
}
