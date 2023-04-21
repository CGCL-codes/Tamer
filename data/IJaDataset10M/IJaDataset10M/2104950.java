package br.org.ged.direto.controller.admin;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import atg.taglib.json.util.JSONArray;
import atg.taglib.json.util.JSONException;
import atg.taglib.json.util.JSONObject;
import br.org.ged.direto.model.entity.Conta;
import br.org.ged.direto.model.entity.Usuario;
import br.org.ged.direto.model.service.UsuarioService;

@Controller
public class AdminUsuariosController {

    @Autowired
    private UsuarioService usuarioService;

    private PrintWriter writer = null;

    @ModelAttribute("usuarios")
    public List<Usuario> allUsers() {
        return usuarioService.getAll(new Usuario());
    }

    @RequestMapping(method = RequestMethod.GET, value = "/admin/usuarios.html")
    public String showAllUsers(ModelMap model) {
        return "admin/usuarios";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/admin/consultaUsuario.html")
    public void consultaUsuario(@RequestParam("id") int idUsuario, HttpServletRequest request, HttpServletResponse response) throws IOException, JSONException {
        writer = response.getWriter();
        ;
        JSONObject result = new JSONObject();
        JSONArray array = new JSONArray();
        Usuario usuario = usuarioService.selectById(idUsuario);
        result.put("idUsuario", usuario.getIdUsuario());
        result.put("idPstGrad", usuario.getPstGrad().getIdPstGrad());
        result.put("usuNome", usuario.getUsuNome());
        result.put("usuLogin", usuario.getUsuLogin());
        result.put("usuIdt", usuario.getUsuIdt());
        result.put("usuNGuerra", usuario.getUsuNGuerra());
        result.put("usuPapel", usuario.getUsuPapel());
        result.put("usuSenha", usuario.getUsuSenha());
        Set<Conta> contas = usuario.getContas();
        for (Conta conta : contas) {
            JSONObject obj = new JSONObject();
            obj.put("idConta", conta.getIdConta());
            obj.put("idCarteira", conta.getCarteira().getIdCarteira());
            obj.put("cartAbr", conta.getCarteira().getIdCarteira() + "-" + conta.getCarteira().getCartAbr() + "[" + conta.getCarteira().getOm().getOmAbr() + "]");
            obj.put("isPrincipal", conta.isPrincipal());
            array.put(obj);
        }
        result.put("contas", array);
        response.setHeader("Content-Type", "application/json; charset=ISO-8859-1");
        writer.print(result);
        writer.flush();
        writer.close();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/admin/buscaUsuarios.html")
    public void usuariosJSON(HttpServletRequest request, HttpServletResponse response) throws IOException, JSONException {
        writer = response.getWriter();
        ;
        JSONObject result = new JSONObject();
        JSONArray array = new JSONArray();
        List<Usuario> usuarios = allUsers();
        for (Usuario usuario : usuarios) {
            JSONArray ja = new JSONArray();
            ja.put(usuario.getIdUsuario());
            ja.put(usuario.getPstGrad().getPstgradNome() + " " + usuario.getUsuNGuerra());
            ja.put(usuario.getUsuLogin());
            array.put(ja);
        }
        response.setHeader("Content-Type", "application/json; charset=ISO-8859-1");
        result.put("users", array);
        writer.print(result);
        writer.flush();
        writer.close();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/admin/cadastros/usuarios.html")
    public String addNewUser() {
        return "admin/cadastros/usuarios";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/admin/editar/usuarios.html")
    public String editUser() {
        return "";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/admin/carteiras/usuarios.html")
    public String vincularCarteira() {
        return "";
    }
}
