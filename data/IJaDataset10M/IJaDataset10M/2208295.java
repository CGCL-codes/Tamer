package br.org.ged.direto.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.ModelAndView;
import br.org.direto.util.DocumentosUtil;
import br.org.ged.direto.model.entity.Conta;
import br.org.ged.direto.model.entity.Pastas;
import br.org.ged.direto.model.entity.Usuario;
import br.org.ged.direto.model.entity.exceptions.CarteiraException;
import br.org.ged.direto.model.entity.menus.MenuTopo;
import br.org.ged.direto.model.service.PastasService;
import br.org.ged.direto.model.service.UsuarioService;
import br.org.ged.direto.model.service.menus.IMenuTopo;

@Controller
public abstract class BaseController {

    final int LIMITE_POR_PAGINA = DocumentosUtil.LIMITE_POR_PAGINA;

    protected HttpSession session;

    @Autowired
    private SessionRegistry sessionRegistry;

    @Autowired
    private PastasService pastasService;

    @Autowired
    private IMenuTopo menuTopo;

    @Autowired
    protected UsuarioService usuarioService;

    @ModelAttribute("numUsers")
    public int getNumberOfUsers() {
        return sessionRegistry.getAllPrincipals().size();
    }

    @ModelAttribute("usuario")
    public Usuario getUserLogon(HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        this.session = request.getSession(true);
        Usuario usuario = usuarioService.selectByLogin(auth.getName());
        return usuario;
    }

    public Integer getIdCarteiraFromSession(HttpServletRequest request) throws CarteiraException {
        this.session = request.getSession(true);
        if (session.getAttribute("j_usuario_conta") != null) return new Integer(Integer.parseInt((String) session.getAttribute("j_usuario_conta"))); else {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            Usuario usuario = usuarioService.selectByLogin(auth.getName());
            Set<Conta> contas = usuario.getContas();
            for (Conta conta : contas) {
                if (conta.isPrincipal()) {
                    int idCarteira = conta.getCarteira().getIdCarteira();
                    session.setAttribute("j_usuario_conta", String.valueOf(idCarteira));
                    Usuario usuLogado = (Usuario) auth.getPrincipal();
                    usuLogado.setIdCarteira(idCarteira);
                    return idCarteira;
                }
            }
            if (session.getAttribute("j_usuario_conta") == null) throw new CarteiraException("Usuário com nenhuma carteira principal cadastrada.");
            return 0;
        }
    }

    @ModelAttribute("pastas")
    public Collection<Pastas> todasPastas(HttpServletRequest request) throws Exception {
        Integer idCarteira = this.getIdCarteiraFromSession(request);
        return this.pastasService.pastasComNrDocumentos(idCarteira);
    }

    @ModelAttribute("contaAtual")
    public int getIdCarteira() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario o = (Usuario) auth.getPrincipal();
        return o.getIdCarteira();
    }

    public Usuario getUsuarioLogado() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (Usuario) auth.getPrincipal();
    }

    @ModelAttribute("menuTopo")
    public Collection<MenuTopo> menuTopo() {
        Collection<MenuTopo> menu = new ArrayList<MenuTopo>();
        try {
            menu = menuTopo.filterMenuTopo(menuTopo.getMenuTopo());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return menu;
    }

    @ExceptionHandler(CarteiraException.class)
    public ModelAndView carteiraException(CarteiraException ex) {
        ModelAndView mav = new ModelAndView("error");
        mav.addObject("error", ex.getMessage());
        return mav;
    }
}
