package com.bcurtu.amigo.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.lang.System;
import org.apache.commons.mail.HtmlEmail;
import org.apache.log4j.Logger;
import com.bcurtu.amigo.db.GrupoDao;
import com.bcurtu.amigo.pojo.Amigo;
import com.bcurtu.amigo.pojo.Config;
import com.bcurtu.amigo.pojo.Grupo;
import com.bcurtu.amigo.pojo.Message;

public class AmigoInvisibleBOImpl implements AmigoInvisibleBO {

    Logger log = Logger.getLogger(this.getClass());

    private Config config;

    private Integer maxTries;

    private GrupoDao dao;

    public GrupoDao getDao() {
        return dao;
    }

    public void setDao(GrupoDao dao) {
        this.dao = dao;
    }

    public void setMaxTries(Integer maxTries) {
        this.maxTries = maxTries;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    public String sortea(List<Amigo> amigos, Message message) throws Exception {
        boolean b = desordena2(amigos);
        Integer id = null;
        if (b) {
            Grupo g = new Grupo();
            g.setBody(message.getBody());
            g.setSubject(message.getSubject());
            g.setDate(new Date());
            g.setAmigos(amigos);
            List domains = new ArrayList();
            for (Iterator<Amigo> it = amigos.iterator(); it.hasNext(); ) {
                Amigo amic = it.next();
                amic.setGrupo(g);
                log.info("Amigo (" + amic.getId() + "," + amic.getName() + "," + amic.getExclude() + ")=>" + amic.getAmigo());
                log.debug("Enviando email a " + amic.getEmail());
                HtmlEmail email = new HtmlEmail();
                email.setHostName(config.getSmtp());
                email.setSmtpPort(config.getPort());
                email.setAuthentication(config.getLogin(), config.getPassword());
                email.addTo(amic.getEmail(), amic.getName());
                email.setFrom(config.getFrom(), config.getNombre());
                email.setSubject(buildSubject(message.getSubject(), amic.getName()));
                email.setHtmlMsg(buildBodyMsg(message.getBody(), amic.getAmigo(), amic.getEmail()));
                String domain = amic.getEmail().substring(amic.getEmail().indexOf("@"));
                if (domains.contains(domain)) {
                    Thread.sleep(100);
                    log.debug("Same domain, sleeping:" + domain);
                } else {
                    domains.add(domain);
                }
                email.send();
                log.debug("Email enviado");
            }
            id = persist(g);
        } else {
            log.warn("Situaci�n de bloqueo al sortear!");
            for (Amigo a : amigos) {
                log.info(a);
            }
            throw new BlockingException();
        }
        return "" + id;
    }

    private String buildSubject(String subject, String nombre) {
        String cleanBody = subject;
        if (cleanBody.length() > 50) {
            cleanBody = cleanBody.substring(0, 50);
        }
        if (cleanBody.indexOf("javascript") > 0) {
            cleanBody = cleanBody.replaceAll("javascript", "");
        }
        if (cleanBody.indexOf(" href") > 0) {
            cleanBody = cleanBody.replaceAll(" href", "");
        }
        if (cleanBody.indexOf("http") > 0) {
            cleanBody = cleanBody.replaceAll("http", "");
        }
        if (cleanBody.indexOf("mailto") > 0) {
            cleanBody = cleanBody.replaceAll("mailto", "");
        }
        if (cleanBody.indexOf("www") > 0) {
            cleanBody = cleanBody.replaceAll("www", "");
        }
        StringBuffer bodysb = new StringBuffer(cleanBody);
        String msg = bodysb.toString().replaceAll("\\$NOMBRE", nombre);
        log.debug("Asunto a enviar: " + msg);
        return msg;
    }

    private String buildBodyMsg(String body, String amigo, String email) {
        String cleanBody = "<html><body><p>" + body;
        if (cleanBody.length() > 150) {
            cleanBody = cleanBody.substring(0, 150);
        }
        if (cleanBody.indexOf("javascript") > 0) {
            cleanBody = cleanBody.replaceAll("javascript", "");
        }
        if (cleanBody.indexOf(" href") > 0) {
            cleanBody = cleanBody.replaceAll(" href", "");
        }
        if (cleanBody.indexOf("src=") > 0) {
            cleanBody = cleanBody.replaceAll("src=", "");
        }
        if (cleanBody.indexOf("http") > 0) {
            cleanBody = cleanBody.replaceAll("http", "");
        }
        if (cleanBody.indexOf("mailto") > 0) {
            cleanBody = cleanBody.replaceAll("mailto", "");
        }
        if (cleanBody.indexOf("www") > 0) {
            cleanBody = cleanBody.replaceAll("www", "");
        }
        StringBuffer bodysb = new StringBuffer(cleanBody);
        bodysb.append("</p>");
        Random rnd = new Random();
        int r = rnd.nextInt();
        bodysb.append("<p>No sabes qué regalarle a " + amigo + "?. Te sugerimos una de nuestras ofertas: </p>");
        bodysb.append("<a href='http://www.dooplan.com/compra-colectiva/bienvenido/?email=" + email + "&utm_medium=email&utm_source=aio'>");
        bodysb.append("<img alt='(Pulsa en ver imágenes para ver la oferta)' src='http://www.dooplan.com/site_media/_img/banner728x90.gif' />");
        bodysb.append("</a>");
        bodysb.append("</p><p>----------------------------------------------------------------------------------------------------------</p>");
        bodysb.append("<p>Mensaje generado automáticamente utilizando www.amigoinvisibleonline.com</p>");
        bodysb.append("</body></html>");
        String msg = bodysb.toString().replaceAll("\\$AMIGO", amigo);
        log.debug("Mensaje a enviar:" + msg);
        return msg;
    }

    private int getInd(int mayor, int i, Collection<Integer> hist) {
        log.debug("Obteniendo un nuevo indice, maximo=" + mayor + " No vale:" + i);
        Random rnd = new Random();
        int r = i;
        do {
            r = rnd.nextInt(mayor);
            log.debug("Probamos con " + r);
            if (hist.contains(r)) {
                r = i;
                log.debug("No vale.Ya está usado");
            }
        } while (r == i);
        hist.add(r);
        if (hist.size() == mayor - 1 && !(hist.contains(mayor - 1))) {
            r = -1;
            log.debug("Atencion, solo queda libre el ultimo! (-1)");
        }
        return r;
    }

    private void desordena(List<Amigo> amigos) {
        int size = amigos.size();
        HashMap<Integer, Amigo> listaFinal = new HashMap<Integer, Amigo>();
        Collection<Integer> hist = new ArrayList<Integer>();
        log.info("Empezamos a ordenar");
        int i = 0;
        while (i < size) {
            int dest = getInd(size, i, hist);
            if (dest == amigos.get(i).getExclude() || dest == -1) {
                log.debug("(" + dest + ")Mala ordenacion, volvemos a empezar...");
                listaFinal = new HashMap<Integer, Amigo>();
                hist = new ArrayList<Integer>();
                i = 0;
            } else {
                log.debug("Ponemos el " + i + " en " + dest);
                listaFinal.put(dest, amigos.get(i));
                i++;
            }
        }
        log.info("Obtenido el orden final, creando la relacion");
        for (i = 0; i < size; i++) {
            Amigo amic = amigos.get(i);
            amic.setAmigo((listaFinal.get(i)).getName());
            log.info("Amigo (" + amic.getId() + "," + amic.getName() + "," + amic.getExclude() + ")=>" + amic.getAmigo());
        }
    }

    public boolean desordena2(List<Amigo> amigos) {
        int tries = amigos.size() + maxTries;
        boolean done = false;
        Random rnd = new Random();
        while (!done && tries > 0) {
            log.debug("Desordenando. Intentos pendientes:" + tries);
            done = true;
            tries--;
            List<Amigo> unsorted = new ArrayList<Amigo>();
            for (Amigo a : amigos) {
                a.setRandom(rnd.nextInt());
                unsorted.add(a);
            }
            Collections.sort(unsorted);
            for (int i = 0; i < amigos.size(); i++) {
                if (unsorted.get(i).getId().intValue() == amigos.get(i).getId().intValue()) {
                    log.debug("Se ha tocado a si mismo. Repetimos.");
                    done = false;
                    break;
                }
                if (unsorted.get(i).getId().intValue() == amigos.get(i).getExclude().intValue()) {
                    log.debug("Le ha tocado a su excluido. Repetimos");
                    done = false;
                    break;
                }
                amigos.get(i).setAmigo(unsorted.get(i).getName());
            }
        }
        log.debug("Finalizado el proceso con exito=" + done);
        return done;
    }

    public Integer persist(Grupo g) throws Exception {
        Integer id = dao.save(g);
        return id;
    }

    public List<Amigo> recupera(Integer grupo) throws Exception {
        return dao.getAmigos(grupo);
    }

    public void reenvia(Amigo amic) throws Exception {
        Grupo g = dao.getGroup(amic.getGrupo().getId());
        log.info("Amigo (" + amic.getId() + "," + amic.getName() + "," + amic.getExclude() + ")=>" + amic.getAmigo());
        log.debug("Enviando email a " + amic.getEmail());
        HtmlEmail email = new HtmlEmail();
        email.setHostName(config.getSmtp());
        email.setSmtpPort(config.getPort());
        email.setAuthentication(config.getLogin(), config.getPassword());
        email.addTo(amic.getEmail(), amic.getName());
        email.setFrom(config.getFrom(), config.getNombre());
        email.setSubject(buildSubject(g.getSubject(), amic.getName()));
        email.setHtmlMsg(buildBodyMsg(g.getBody(), amic.getAmigo(), amic.getEmail()));
        email.send();
        log.debug("Email enviado");
    }

    public Amigo getAmigo(Integer amigoId) throws Exception {
        return dao.getAmigo(amigoId);
    }
}
