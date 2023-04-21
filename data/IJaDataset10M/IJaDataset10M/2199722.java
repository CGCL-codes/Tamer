package com.cibertec.project.action;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import com.cibertec.project.bean.AlternativaDTO;
import com.cibertec.project.bean.ExamenDTO;
import com.cibertec.project.bean.NivelDTO;
import com.cibertec.project.bean.PersonaDTO;
import com.cibertec.project.bean.PreguntaDTO;
import com.cibertec.project.bean.ResultadoDTO;
import com.cibertec.project.bean.TemaDTO;
import com.cibertec.project.common.Constant;
import com.cibertec.project.dao.ExamenDAO;
import com.cibertec.project.dao.jpa.ExamenJPADAO;
import com.cibertec.project.service.ApplicationBusinessDelegate;
import com.cibertec.project.service.NivelService;
import com.cibertec.project.service.PreguntaService;
import com.cibertec.project.service.ResultadoService;
import com.cibertec.project.service.TemaService;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class ExamenAction extends ActionSupport {

    /**
	 * 
	 */
    private static final Logger log = Logger.getLogger(ExamenAction.class);

    private static final long serialVersionUID = 1L;

    private static ApplicationBusinessDelegate abd = new ApplicationBusinessDelegate();

    private static TemaService temaService = abd.getTemaService();

    private static NivelService nivelService = abd.getNivelService();

    private static PreguntaService preguntaService = abd.getPreguntaService();

    private static ResultadoService resultadoService = abd.getResultadoService();

    List<TemaDTO> temas;

    List<NivelDTO> niveles;

    List<PreguntaDTO> preguntas;

    List<AlternativaDTO> alternativaList;

    ExamenDTO examen;

    PreguntaDTO preguntaNum = new PreguntaDTO();

    PreguntaDTO preguntaExam;

    Map<String, Object> examenMap = ActionContext.getContext().getSession();

    ResultadoDTO resultadoDTO = new ResultadoDTO();

    String respuesta;

    private boolean commandAtras = false;

    public String loadExam() {
        try {
            temas = temaService.obtenerTemas();
            niveles = nivelService.obtenerNivel();
        } catch (Exception e) {
        }
        return "success";
    }

    public String loadPreguntas() {
        try {
            preguntas = preguntaService.PreguntaWithAlternativas(preguntaExam);
            Collections.shuffle(preguntas);
            for (int i = 0; i < preguntas.size(); i++) {
                String num = String.valueOf(i);
                examenMap.put(num, preguntas.get(i));
                log.info(preguntas.get(i).getStrDescripcion());
                for (AlternativaDTO preguntaDTO : preguntas.get(i).getAlternativas()) {
                    log.info(preguntaDTO.getStrDescripcion());
                }
            }
            log.info("tama�o: " + examenMap.size());
            preguntaExam = (PreguntaDTO) examenMap.get(0);
            alternativaList = preguntaExam.getAlternativasList();
            preguntaNum.setNumero(0);
        } catch (Exception e) {
        }
        return "success";
    }

    public String next() {
        log.info("respuesta: " + respuesta);
        ((PreguntaDTO) examenMap.get(preguntaNum.getNumero())).setStrRespuesta(respuesta);
        PreguntaDTO p = (PreguntaDTO) examenMap.get(preguntaNum.getNumero());
        log.info("antes de pasar: " + p.getStrDescripcion());
        log.info("resp anterior: " + p.getStrRespuesta());
        log.info("num actual: " + preguntaNum.getNumero());
        log.info("total: " + examenMap.size());
        if (preguntaNum.getNumero() < examenMap.size() - 2) {
            log.info("csm");
            preguntaNum.setNumero(preguntaNum.getSiguienteNumero());
            preguntaExam = (PreguntaDTO) examenMap.get(String.valueOf(preguntaNum.getNumero()));
            alternativaList = preguntaExam.getAlternativasList();
            log.info("next");
            log.info(preguntaNum.getNumero());
            log.info(preguntaExam);
            preguntaNum.setNumero(preguntaNum.getNumero());
        } else preguntaExam = null;
        validaBotones();
        return "success";
    }

    public String back() {
        preguntaNum.setNumero(preguntaNum.getAnteriorNumero());
        preguntaExam = (PreguntaDTO) examenMap.get(String.valueOf(preguntaNum.getNumero()));
        alternativaList = preguntaExam.getAlternativasList();
        log.info("back");
        log.info(preguntaNum.getNumero());
        log.info(preguntaExam);
        preguntaNum.setNumero(preguntaNum.getNumero());
        validaBotones();
        return "success";
    }

    public String verResultado() throws Exception {
        ExamenDAO exmService = new ExamenJPADAO();
        int ptjeTotal = 0;
        int numCorrectas = 0;
        int numIncorrectas = examenMap.size();
        int codTema = 2;
        try {
            for (Object p : examenMap.values()) {
                PreguntaDTO pregunta = (PreguntaDTO) p;
                log.info("Pregunta: " + pregunta.getStrDescripcion());
                log.info("ALTERNATIVAS Y RESPUESTAS");
                for (AlternativaDTO a : pregunta.getAlternativas()) {
                    log.info("alternativa:" + a.getStrDescripcion());
                    if (a.getIntTipo() == 1 && pregunta.getStrRespuesta().equals("" + a.getIntCodAlter())) {
                        numCorrectas++;
                        numIncorrectas--;
                        log.info("acerto esta!");
                        log.info("Sumando puntaje");
                        ptjeTotal += pregunta.getDbPuntaje();
                        log.info("puntaje acumulado: " + ptjeTotal);
                    }
                }
                codTema = pregunta.getIntTema();
                log.info("Codigo de Respuesta marcada: " + pregunta.getStrRespuesta());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("TOTAL DE CORRECTAS:" + numCorrectas);
        log.info("TOTAL DE INCORRECTAS:" + numIncorrectas);
        log.info("PUNTAJE TOTAL:" + ptjeTotal);
        PersonaDTO personaDTO = new PersonaDTO();
        Map session = ActionContext.getContext().getSession();
        personaDTO = (PersonaDTO) session.get(Constant.USUARIO_ACTUALMENTE_LOGUEADO);
        resultadoDTO.setIntExamen(codTema);
        resultadoDTO.setIntPersona(personaDTO.getIntCodPersona());
        resultadoDTO.setDbPuntaje(ptjeTotal);
        resultadoDTO.setStrFecReg(Constant.getTimeNow());
        resultadoDTO.setIntRespBuenas(numCorrectas);
        resultadoDTO.setIntRespMalas(numIncorrectas);
        resultadoService.insertResultado(resultadoDTO);
        return SUCCESS;
    }

    private void validaBotones() {
        if (preguntaNum.getNumero() > 0) setCommandAtras(true); else setCommandAtras(false);
    }

    public List<TemaDTO> getTemas() {
        return temas;
    }

    public void setTemas(List<TemaDTO> temas) {
        this.temas = temas;
    }

    public List<NivelDTO> getNiveles() {
        return niveles;
    }

    public void setNiveles(List<NivelDTO> niveles) {
        this.niveles = niveles;
    }

    public List<PreguntaDTO> getPreguntas() {
        return preguntas;
    }

    public void setPreguntas(List<PreguntaDTO> preguntas) {
        this.preguntas = preguntas;
    }

    public ExamenDTO getExamen() {
        return examen;
    }

    public void setExamen(ExamenDTO examen) {
        this.examen = examen;
    }

    public Map<String, Object> getExamenMap() {
        return examenMap;
    }

    public void setExamenMap(Map<String, Object> examenMap) {
        this.examenMap = examenMap;
    }

    public PreguntaDTO getPreguntaNum() {
        return preguntaNum;
    }

    public void setPreguntaNum(PreguntaDTO preguntaNum) {
        this.preguntaNum = preguntaNum;
    }

    public PreguntaDTO getPreguntaExam() {
        return preguntaExam;
    }

    public void setPreguntaExam(PreguntaDTO preguntaExam) {
        this.preguntaExam = preguntaExam;
    }

    public List<AlternativaDTO> getAlternativaList() {
        return alternativaList;
    }

    public void setAlternativaList(List<AlternativaDTO> alternativaList) {
        this.alternativaList = alternativaList;
    }

    public String getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }

    public boolean isCommandAtras() {
        return commandAtras;
    }

    public void setCommandAtras(boolean commandAtras) {
        this.commandAtras = commandAtras;
    }

    public ResultadoDTO getResultadoDTO() {
        return resultadoDTO;
    }

    public void setResultadoDTO(ResultadoDTO resultadoDTO) {
        this.resultadoDTO = resultadoDTO;
    }
}
