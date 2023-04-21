package br.com.klis.batendoumabola.server;

import java.util.ArrayList;
import br.com.klis.batendoumabola.client.exception.DaoException;
import br.com.klis.batendoumabola.client.exception.NotLoggedInException;
import br.com.klis.batendoumabola.client.service.ImagemDoBateBolaService;
import br.com.klis.batendoumabola.server.model.ImagemDoBateBola;
import br.com.klis.batendoumabola.server.util.ImageUtil;
import br.com.klis.batendoumabola.shared.DadosDaImagem;
import br.com.klis.batendoumabola.shared.ImagemUrl;
import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Query;

@SuppressWarnings("serial")
public class ImagemDoBateBolaServiceImpl extends RemoteServiceServlet implements ImagemDoBateBolaService {

    static {
        ObjectifyService.register(ImagemDoBateBola.class);
    }

    public ArrayList<DadosDaImagem> getImagensDoBateBola(Long bateBolaId) throws NotLoggedInException {
        Objectify ofy = ObjectifyService.begin();
        ArrayList<DadosDaImagem> imagens = new ArrayList<DadosDaImagem>();
        Query<ImagemDoBateBola> q = ofy.query(ImagemDoBateBola.class).filter("bateBolaId", bateBolaId);
        for (ImagemDoBateBola i : q) {
            imagens.add(convertToDadosDaImagem(i));
        }
        return imagens;
    }

    @Override
    public Boolean delete(DadosDaImagem dados) throws NotLoggedInException {
        checkLoggedIn();
        Objectify ofy = ObjectifyService.begin();
        ImagemDoBateBola imagem = ofy.find(ImagemDoBateBola.class, dados.getId());
        if (imagem != null) {
            ofy.delete(imagem);
            return true;
        } else {
            return false;
        }
    }

    private DadosDaImagem convertToDadosDaImagem(ImagemDoBateBola ibb) {
        DadosDaImagem dados = new DadosDaImagem();
        dados.setBateBolaId(ibb.getBateBolaId());
        dados.setContentType(ibb.getContentType());
        dados.setCreation(ibb.getCreation());
        dados.setFileName(ibb.getFileName());
        dados.setId(ibb.getId());
        dados.setSize(ibb.getSize());
        dados.setUrl(ImagemUrl.url + ibb.getId().toString());
        dados.setFieldName(ibb.getFieldName());
        dados.setHeight(ibb.getHeight());
        dados.setWidth(ibb.getWidth());
        return dados;
    }

    private ImagemDoBateBola convertToImagemDoBateBola(DadosDaImagem ddi) {
        ImagemDoBateBola imagem = new ImagemDoBateBola();
        imagem.setBateBolaId(ddi.getBateBolaId());
        imagem.setContentType(ddi.getContentType());
        imagem.setCreation(ddi.getCreation());
        imagem.setFileName(ddi.getFileName());
        imagem.setId(ddi.getId());
        imagem.setSize(ddi.getSize());
        imagem.setConteudo(new Blob(ddi.getConteudo()));
        imagem.setFieldName(ddi.getFieldName());
        imagem.setWidth(ddi.getWidth());
        imagem.setHeight(ddi.getHeight());
        return imagem;
    }

    public DadosDaImagem save(DadosDaImagem dados) throws NotLoggedInException, DaoException {
        checkLoggedIn();
        byte[] conteudo = dados.getConteudo();
        ImagemDoBateBola imagem = convertToImagemDoBateBola(dados);
        imagem.setThumb(new Blob(ImageUtil.resizeImage(conteudo, 60, 60)));
        Objectify ofy = ObjectifyService.begin();
        ofy.put(imagem);
        dados.setId(imagem.getId());
        System.out.println("Imagem final - tam conteudo: " + imagem.getConteudo().getBytes().length);
        System.out.println("Imagem final - tam thumb: " + imagem.getThumb().getBytes().length);
        return dados;
    }

    private void checkLoggedIn() throws NotLoggedInException {
        if (getUser() == null) {
            throw new NotLoggedInException("Not logged in.");
        }
    }

    private User getUser() {
        UserService userService = UserServiceFactory.getUserService();
        return userService.getCurrentUser();
    }
}
