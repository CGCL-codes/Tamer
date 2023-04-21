package com.kmlitro.manager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.kmlitro.entity.Abastecimento;
import com.kmlitro.entity.Usuario;
import com.kmlitro.entity.Veiculo;
import com.kmlitro.entity.dao.AbastecimentoDAO;
import com.kmlitro.entity.dao.VeiculoDAO;
import com.kmlitro.services.KmLitroServices;

public class KmLitroMobileManager extends KmLitroServices {

    private static final String MOBILE_INDEX = "/mobile/indexMobile.jsp";

    private static final String MOBILE_LOGGED_INDEX = "/mobile/index_logged.jsp";

    private static final String MOBILE_ABASTECIMENTO_REGISTER = "/mobile/registerAbastecimento.jsp";

    private static final CharSequence INDEX = "mobile.mkml";

    private static final CharSequence LOGIN = "/mobile/login.mkml";

    private static final CharSequence REGISTER_ABASTECIMENTO = "/mobile/abastecer.mkml";

    private static final CharSequence LOGOUT = "/mobile/logout.mkml";

    private HttpServletRequest request;

    private HttpServletResponse response;

    private HttpServlet action;

    public KmLitroMobileManager(HttpServlet action, HttpServletRequest request, HttpServletResponse response) {
        this.setAction(action);
        this.request = request;
        this.response = response;
    }

    public KmLitroMobileManager(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }

    public KmLitroMobileManager(HttpServletRequest request) {
        this.request = request;
    }

    private void dispatch(String page) throws Exception {
        RequestDispatcher dispatcher = request.getRequestDispatcher(page);
        try {
            dispatcher.forward(request, response);
        } catch (ServletException e) {
            throw new Exception(e);
        } catch (IOException e) {
            throw new Exception(e);
        }
    }

    public void setAction(HttpServlet action) {
        this.action = action;
    }

    public HttpServlet getAction() {
        return action;
    }

    public void execute(String cmd) throws Exception {
        if (cmd.contains(INDEX)) {
            this.redirectMobile();
        } else if (cmd.contains(LOGIN)) {
            this.login();
        } else if (cmd.contains(REGISTER_ABASTECIMENTO)) {
            this.register();
        } else if (cmd.contains(LOGOUT)) {
            this.logout();
        }
    }

    private void redirectMobile() throws Exception {
        response.sendRedirect(MOBILE_INDEX);
        System.out.println("redirecione para o mobile...");
    }

    public void login() throws Exception {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        List<Veiculo> veiculos = new ArrayList<Veiculo>();
        VeiculoDAO vDAO = new VeiculoDAO();
        if ((null != username) && (null != password)) {
            try {
                Usuario usuario = super.login(username, password);
                veiculos = vDAO.selectVeiculos(usuario.getId());
                {
                    if (null != usuario && usuario.getId() != 0) {
                        try {
                            request.getSession().setAttribute("usuario", usuario);
                            request.getSession().setAttribute("veiculos", veiculos);
                            dispatch(MOBILE_LOGGED_INDEX);
                        } catch (Exception e) {
                            e.printStackTrace();
                            dispatch(MOBILE_INDEX);
                            request.setAttribute("loginError", "Nome/Usu�rio inv�lidos!");
                            request.setAttribute("username", username);
                            throw new Exception("Nome/Usu�rio inv�lidos!");
                        }
                    } else {
                        request.setAttribute("loginError", "Usu�rio/Senha inv�lidos!");
                        request.setAttribute("username", username);
                        dispatch(MOBILE_INDEX);
                    }
                }
            } catch (Exception e) {
                dispatch("error.jsp");
            }
        } else {
            dispatch(MOBILE_INDEX);
        }
    }

    private void logout() throws Exception {
        request.getSession().invalidate();
        this.dispatch(MOBILE_INDEX);
    }

    private void register() throws Exception {
        Abastecimento abastecimento = new Abastecimento();
        Abastecimento hodometroAbastecimento = new Abastecimento();
        @Deprecated List<Veiculo> veiculos = new ArrayList<Veiculo>();
        Map<String, String> errors = validateAbastecimento(abastecimento);
        if ((abastecimento.getCarro() > 0) && (abastecimento.getUsuario() > 0)) {
            if (errors.size() > 0) {
                request.setAttribute("errors", errors);
                request.setAttribute("abastecimentoRegister", abastecimento);
                this.dispatch(MOBILE_ABASTECIMENTO_REGISTER);
                return;
            }
            try {
                AbastecimentoDAO aDAO = new AbastecimentoDAO();
                VeiculoDAO vDAO = new VeiculoDAO();
                hodometroAbastecimento = aDAO.selectHodometroUltimoAbastecimento(abastecimento.getCarro());
                String hodometro = hodometroAbastecimento.getHodometro();
                if (hodometroAbastecimento != null && hodometro != null) {
                    if (Integer.parseInt(abastecimento.getHodometro()) < Integer.parseInt(hodometro)) {
                        request.setAttribute("registeredAbastecimento", false);
                        request.setAttribute("errorCause", "N�o foi poss�vel cadastrar o abastecimento, pois a kilometragem inserida � menor que a anterior");
                        request.setAttribute("abastecimentoRegister", abastecimento);
                        this.dispatch(MOBILE_ABASTECIMENTO_REGISTER);
                        return;
                    } else {
                        abastecimento.setDif(Integer.parseInt(abastecimento.getHodometro()) - Integer.parseInt(hodometro));
                    }
                }
                vDAO.updateHodometro(abastecimento.getCarro(), abastecimento.getHodometro());
                aDAO.createAbastecimento(abastecimento);
                veiculos = vDAO.selectVeiculos(abastecimento.getUsuario());
                request.getSession().setAttribute("veiculos", veiculos);
                request.setAttribute("registeredAbastecimento", true);
                dispatch(MOBILE_ABASTECIMENTO_REGISTER);
            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("registeredAbastecimento", false);
                request.setAttribute("errorCause", "N�o foi poss�vel cadastrar o abastecimento, sei l� porque.");
            }
        } else {
            dispatch(MOBILE_ABASTECIMENTO_REGISTER);
        }
    }

    private Map<String, String> validateAbastecimento(Abastecimento abastecimento) {
        Map<String, String> errors = new HashMap<String, String>();
        String veiculo = request.getParameter("selectVeiculo");
        String combustivel = request.getParameter("selectCombustivel");
        String hodometro = request.getParameter("txtHodometro");
        String litros = request.getParameter("txtLitros");
        String precoLitro = request.getParameter("txtPrecoLitro");
        String userId = request.getParameter("userId");
        if (veiculo != null) {
            abastecimento.setCarro(Integer.parseInt(veiculo));
        } else {
            errors.put("veiculo", "Informe o ve�culo corretamente");
        }
        if (combustivel != null) {
            abastecimento.setCombustivel(Integer.parseInt(combustivel));
        } else {
            errors.put("combustivel", "Informe o combustivel corretamente");
        }
        if (hodometro == null || hodometro.length() < 1) {
            errors.put("hodometro", "Informe o hodometro(Kilometragem) corretamente");
        } else {
            hodometro = hodometro.replace(".", "");
            abastecimento.setHodometro(hodometro);
        }
        if (litros == null || litros.length() < 1) {
            errors.put("litros", "Informe os litros abastecidos corretamente");
        } else {
            litros = litros.replace(",", ".");
            abastecimento.setLitros(Float.parseFloat(litros));
        }
        if (precoLitro == null || precoLitro.length() < 4) {
            errors.put("veiculo", "Informe o ve�culo corretamente");
        } else {
            precoLitro = precoLitro.replace(",", ".");
            abastecimento.setPrecoLitro(Float.parseFloat(precoLitro));
        }
        if (litros != null && precoLitro != null && (litros != "" && precoLitro != "")) {
            Float total;
            total = Float.parseFloat(litros) * Float.parseFloat(precoLitro);
            abastecimento.setPrecoTotal(total);
        }
        if (userId != null) {
            abastecimento.setUsuario(Integer.parseInt(userId));
        }
        return errors;
    }
}
