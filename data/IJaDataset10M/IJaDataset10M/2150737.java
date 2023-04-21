package der.ponto;

import entities.dao.DAOConstraintException;
import entities.dao.DAOException;
import entities.dao.DAOFactory;
import entities.dao.DAOValidationException;
import entities.dao.IDAO;
import java.sql.Time;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.junit.Test;
import util.convert.Convert;
import static org.junit.Assert.*;

public class TestModelPonto {

    public static final Date TODAY = Calendar.getInstance().getTime();

    public static final Time TIME = new Time(10, 22, 0);

    public TestModelPonto() {
    }

    @Test
    public void batidaTest() throws ParseException, DAOException, DAOValidationException, DAOConstraintException {
        Batida batida = new Batida();
        batida.getId().setData(Convert.stringToDate("1/1/1957"));
        batida.getId().setHora(new Time(10, 22, 0));
        batida.getId().setNumero(39361);
        batida.setNumeroRelogio(Short.valueOf("2"));
        System.out.println(batida.toString());
        IDAO dao = DAOFactory.getInstance().getDAO(Batida.class);
        long size = dao.queryCount("der.ponto.Batida");
        dao.save(batida);
        assertEquals(size + 1, dao.queryCount("der.ponto.Batida"));
        dao.delete(batida);
        assertEquals(size, dao.queryCount("der.ponto.Batida"));
    }

    @Test
    public void feriadoTest() throws ParseException, DAOException, DAOValidationException, DAOConstraintException {
        Feriado feriado = new Feriado();
        feriado.setDataFeriado(Convert.stringToDate("1/7/1957"));
        feriado.setDescricao("Ano novo");
        IDAO dao = DAOFactory.getInstance().getDAO(Feriado.class);
        int size = dao.query("der.ponto.Feriado").size();
        dao.save(feriado);
        assertEquals(size + 1, dao.query("der.ponto.Feriado").size());
        dao.delete(feriado);
        assertEquals(size, dao.query("der.ponto.Feriado").size());
    }

    @Test
    public void gradeTest() throws ParseException {
    }

    @Test
    public void lotacaoTest() throws DAOException, DAOValidationException, DAOConstraintException {
        Lotacao lotacao = new Lotacao();
        lotacao.setCodigo("XYZ");
        lotacao.setDescricao("CELULA DE TIC");
        lotacao.setSigla("CETIC");
        IDAO dao = DAOFactory.getInstance().getDAO(Lotacao.class);
        int size = dao.query("der.ponto.Lotacao").size();
        dao.save(lotacao);
        assertEquals(size + 1, dao.query("der.ponto.Lotacao").size());
        dao.delete(lotacao);
        assertEquals(size, dao.query("der.ponto.Lotacao").size());
    }

    @Test
    public void mesTest() throws ParseException, DAOException, DAOValidationException, DAOConstraintException {
        MesProcessado mes = new MesProcessado();
        mes.setDataMes(Convert.stringToDate("1/7/1957"));
        IDAO dao = DAOFactory.getInstance().getDAO(MesProcessado.class);
        int size = dao.query("der.ponto.MesProcessado").size();
        dao.save(mes);
        assertEquals(size + 1, dao.query("der.ponto.MesProcessado").size());
        dao.delete(mes);
        assertEquals(size, dao.query("der.ponto.MesProcessado").size());
    }

    public void funcionarioTest() throws DAOException {
        int totalFunc = 1000;
        Grade grade = (Grade) DAOFactory.getInstance().getDAO(Grade.class).lookup(Grade.class, (short) 1);
        Lotacao lotacao = (Lotacao) DAOFactory.getInstance().getDAO(Lotacao.class).lookup(Lotacao.class, "1");
        IDAO dao = DAOFactory.getInstance().getDAO(Funcionario.class);
        for (Integer i = 1; i <= totalFunc; i++) {
            Funcionario fun = new Funcionario();
            fun.setMatriculaCompleta(i.toString());
            fun.setMatricula(i.toString());
            fun.setCargaHoraria((short) 8);
            fun.setDataInicio(TODAY);
            fun.setGradeHorario(grade);
            fun.setBatePonto((short) 1);
            fun.setNomeCompleto("FUNCIONARIO " + i.toString());
            fun.setNumeroFuncionario(i);
            fun.setLotacao(lotacao);
        }
        List<Funcionario> lista = dao.query("der.ponto.Funcionario");
        assertEquals(totalFunc, lista.size());
        assertEquals("teste", lista.get(0).getGradeHorario().getDescricao());
    }

    public void ocorrenciaTest() throws DAOException, DAOValidationException, DAOConstraintException {
        Funcionario func = (Funcionario) DAOFactory.getInstance().getDAO(Funcionario.class).lookup(Funcionario.class, "1");
        Ocorrencia oco = new Ocorrencia();
        oco.setDataOcorrencia(TODAY);
        oco.setDescricao("TESTE");
        oco.setFuncionario(func);
        oco.setBancoDeHoras((short) 0);
        oco.setMotivo("A");
        IDAO dao = DAOFactory.getInstance().getDAO(Ocorrencia.class);
        dao.save(oco);
        Ocorrencia oco2 = new Ocorrencia();
        oco2.setId(oco.getId());
        dao.delete(oco2);
    }

    @Test
    public void funcionarioReportTest() {
        Lotacao lotacao = new Lotacao();
        lotacao.setCodigo("02");
        lotacao.setDescricao("Teste");
        lotacao.setSigla("TST");
        Funcionario funcionario = new Funcionario();
        funcionario.setMatriculaCompleta("56565565");
        funcionario.setNomeCompleto("Pe Grande das Neves");
        funcionario.setBatePonto(new Short("0"));
        funcionario.setLotacao(lotacao);
        funcionario.setDataInicio(new Date());
        FuncionarioReport funcionarioReport = new FuncionarioReport(funcionario.getMatriculaCompleta(), funcionario.getNomeCompleto(), funcionario.getNumeroFuncionario(), funcionario.getLotacao(), funcionario.getDataInicio(), funcionario.getBatePonto());
        System.out.println(funcionarioReport);
    }

    @Test
    public void StatusOcorrenciaTest() throws DAOException, DAOValidationException, DAOConstraintException {
        IDAO dao = DAOFactory.getInstance().getDAO(StatusOcorrencia.class);
        int size = dao.query("der.ponto.StatusOcorrencia").size();
        assertEquals(size, 4);
    }
}
