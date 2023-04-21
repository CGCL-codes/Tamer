package br.ufc.quixada.adrs.comandos.visit;

import br.ufc.quixada.adrs.interfaces.Comando;
import br.ufc.quixada.adrs.model.Adrs;
import br.ufc.quixada.adrs.model.Estado;
import br.ufc.quixada.adrs.model.Municipio;
import br.ufc.quixada.adrs.service.AdrsService;
import br.ufc.quixada.adrs.service.EstadoService;
import br.ufc.quixada.adrs.service.MunicipioService;
import br.ufc.quixada.adrs.util.Msg;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Welligton
 */
public class CmdVisitMontaRelatorioPassoUm implements Comando {

    public String executa(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(true);
        String estado = (String) request.getParameter("estado");
        String municipio = (String) request.getParameter("cidade");
        String retornoAnterior = (String) session.getAttribute("retornoAnterior1");
        String retorno = request.getParameter("retorno");
        Estado estadoSele = null;
        Municipio municipioSele = null;
        AdrsService aServ = new AdrsService();
        EstadoService estadoService = new EstadoService();
        MunicipioService municipioService = new MunicipioService();
        List<Adrs> listAdrs = null;
        List<Municipio> municipiosSelecionados = new ArrayList<Municipio>();
        List<Estado> estadosSelecionados = new ArrayList<Estado>();
        if (estado == null || estado.trim().equals("-10")) {
            session.setAttribute("erro", Msg.EMPTY_SELECTION_ERROR_ESTADO);
            return "/visit/" + retornoAnterior + ".jsp";
        }
        if (estado.equals("TODOS") || estado.trim().equals("-1")) {
            estadoSele = new Estado();
            estadoSele.setId(-1l);
            estadoSele.setNome("TODOS");
            municipioSele = new Municipio();
            municipioSele.setNome("TODOS");
            municipioSele.setId(-1l);
            listAdrs = aServ.getAllAdrs();
            estadosSelecionados = estadoService.getAllEstados();
            for (Estado estadoAux : estadoService.getAllEstados()) {
                municipiosSelecionados.addAll(estadoAux.getMunicipios());
            }
        } else if (municipio.equals("TODOS") || municipio.trim().equals("-1")) {
            estadoSele = new EstadoService().getEstadoById(Long.parseLong(estado));
            municipioSele = new Municipio();
            municipioSele.setNome("TODOS");
            municipioSele.setId(-1l);
            listAdrs = aServ.getByEstadoId(estadoSele.getId());
            estadosSelecionados.add(estadoSele);
            municipiosSelecionados = municipioService.getMunicipiosByEstadoId(Long.parseLong(estado));
        } else {
            estadoSele = new EstadoService().getEstadoById(Long.parseLong(estado));
            municipioSele = new MunicipioService().getMunicipioById(Long.parseLong(municipio));
            municipiosSelecionados.add(municipioSele);
            estadosSelecionados.add(estadoSele);
            listAdrs = aServ.getByMunicipioId(municipioSele.getId());
        }
        if (listAdrs == null || listAdrs.isEmpty()) {
            session.setAttribute("erro", Msg.NOT_FOUND_ERROR_ADRSS);
            return "/visit/" + retornoAnterior + ".jsp";
        }
        String todosMunicipios = "";
        int tamanho = municipiosSelecionados.size();
        int contador = 1;
        for (Municipio municiopioAux : municipiosSelecionados) {
            if (tamanho == 1) {
                todosMunicipios += municiopioAux.getNome();
            } else {
                if (tamanho > contador) {
                    todosMunicipios += municiopioAux.getNome() + ", ";
                } else {
                    todosMunicipios += municiopioAux.getNome();
                }
            }
            contador++;
        }
        String todosEstados = "";
        tamanho = estadosSelecionados.size();
        contador = 1;
        for (Estado estadoAux : estadosSelecionados) {
            if (tamanho == 1) {
                todosEstados += estadoAux.getNome();
            } else {
                if (tamanho > contador) {
                    todosEstados += estadoAux.getNome() + ", ";
                } else {
                    todosEstados += estadoAux.getNome();
                }
            }
            contador++;
        }
        session.setAttribute("estadoSelecionadoQS", todosEstados);
        session.setAttribute("municipioSelecionadoQS", todosMunicipios);
        session.setAttribute("listAdrsQS", listAdrs);
        session.setAttribute("retornoAnterior2", retorno);
        return "/visit/" + retorno + ".jsp";
    }
}
