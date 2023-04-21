package componente.consulta.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import pattern.data_access_object.Atributo;
import pattern.data_access_object.MySQL;
import pattern.util.Data;
import pattern.util.HCalendar;

/**
 * @author Henrick Daniel
 *
 * TODO Para alterar o gabarito desse coment�rio de tipo gerado, v� para
 * Janela - Prefer�ncias - Java - Estilo de C�digo - Gabaritos de C�digo
 */
public class ConsultaDAOMySQL extends MySQL implements ConsultaDAO {

    /**
	 * 
	 */
    public ConsultaDAOMySQL() {
        super(MySQL.MYSQL);
    }

    public int insertConsulta(ConsultaEntityNovo entity) {
        ArrayList atributos = new ArrayList();
        Atributo id_consulta = new Atributo("id_consulta", entity.getId_consulta() + "");
        atributos.add(id_consulta);
        Atributo fk_medico = new Atributo("fk_medico", entity.getFk_medico() + "");
        atributos.add(fk_medico);
        Atributo fk_prontuario = new Atributo("fk_prontuario", entity.getFk_prontuario() + "");
        atributos.add(fk_prontuario);
        Atributo dt_data = new Atributo("dt_data", HCalendar.getStringyyyymmdd(entity.getDt_data()));
        atributos.add(dt_data);
        Atributo de_pupilas = new Atributo("de_pupilas", entity.getDe_pupilas());
        atributos.add(de_pupilas);
        Atributo de_palpebras = new Atributo("de_palpebras", entity.getDe_palpebras());
        atributos.add(de_palpebras);
        Atributo de_conjuntivas = new Atributo("de_conjuntivas", entity.getDe_conjuntivas());
        atributos.add(de_conjuntivas);
        Atributo de_aparelho_lacrimal = new Atributo("de_aparelho_lacrimal", entity.getDe_aparelho_lacrimal());
        atributos.add(de_aparelho_lacrimal);
        Atributo de_cover_test = new Atributo("de_cover_test", entity.getDe_cover_test());
        atributos.add(de_cover_test);
        Atributo de_hirschberg = new Atributo("de_hirschberg", entity.getDe_hirschberg());
        atributos.add(de_hirschberg);
        Atributo de_ppc = new Atributo("de_ppc", entity.getDe_ppc());
        atributos.add(de_ppc);
        Atributo in_rot_od1 = new Atributo("in_rot_od1", entity.getIn_rot_od1() + "");
        atributos.add(in_rot_od1);
        Atributo in_rot_od2 = new Atributo("in_rot_od2", entity.getIn_rot_od2() + "");
        atributos.add(in_rot_od2);
        Atributo in_rot_od3 = new Atributo("in_rot_od3", entity.getIn_rot_od3() + "");
        atributos.add(in_rot_od3);
        Atributo in_rot_od4 = new Atributo("in_rot_od4", entity.getIn_rot_od4() + "");
        atributos.add(in_rot_od4);
        Atributo in_rot_od5 = new Atributo("in_rot_od5", entity.getIn_rot_od5() + "");
        atributos.add(in_rot_od5);
        Atributo in_rot_od6 = new Atributo("in_rot_od6", entity.getIn_rot_od6() + "");
        atributos.add(in_rot_od6);
        Atributo in_rot_oe1 = new Atributo("in_rot_oe1", entity.getIn_rot_oe1() + "");
        atributos.add(in_rot_oe1);
        Atributo in_rot_oe2 = new Atributo("in_rot_oe2", entity.getIn_rot_oe2() + "");
        atributos.add(in_rot_oe2);
        Atributo in_rot_oe3 = new Atributo("in_rot_oe3", entity.getIn_rot_oe3() + "");
        atributos.add(in_rot_oe3);
        Atributo in_rot_oe4 = new Atributo("in_rot_oe4", entity.getIn_rot_oe4() + "");
        atributos.add(in_rot_oe4);
        Atributo in_rot_oe5 = new Atributo("in_rot_oe5", entity.getIn_rot_oe5() + "");
        atributos.add(in_rot_oe5);
        Atributo in_rot_oe6 = new Atributo("in_rot_oe6", entity.getIn_rot_oe6() + "");
        atributos.add(in_rot_oe6);
        Atributo in_tonometria_aplanacao_oe = new Atributo("in_tonometria_aplanacao_oe", entity.getIn_tonometria_aplanacao_oe() + "");
        atributos.add(in_tonometria_aplanacao_oe);
        Atributo in_tonometria_aplanacao_od = new Atributo("in_tonometria_aplanacao_od", entity.getIn_tonometria_aplanacao_od() + "");
        atributos.add(in_tonometria_aplanacao_od);
        Atributo in_tonometria_pneumatica_oe = new Atributo("in_tonometria_pneumatica_oe", entity.getIn_tonometria_pneumatica_oe() + "");
        atributos.add(in_tonometria_pneumatica_oe);
        Atributo in_tonometria_pneumatica_od = new Atributo("in_tonometria_pneumatica_od", entity.getIn_tonometria_pneumatica_od() + "");
        atributos.add(in_tonometria_pneumatica_od);
        Atributo de_tonometria_aplanacao_hora = new Atributo("de_tonometria_aplanacao_hora", entity.getDe_tonometria_aplanacao_hora());
        atributos.add(de_tonometria_aplanacao_hora);
        Atributo dt_auto_data = new Atributo("dt_auto_data", HCalendar.getStringyyyymmdd(entity.getDt_auto_data()));
        atributos.add(dt_auto_data);
        Atributo de_auto_hora = new Atributo("de_auto_hora", entity.getDe_auto_hora());
        atributos.add(de_auto_hora);
        Atributo de_auto_dist_vertice = new Atributo("de_auto_dist_vertice", entity.getDe_auto_dist_vertice());
        atributos.add(de_auto_dist_vertice);
        Atributo de_dist_inter_pupilar = new Atributo("de_dist_inter_pupilar", entity.getDe_dist_inter_pupilar());
        System.out.println("teste na dao .. " + entity.getDe_dist_inter_pupilar());
        atributos.add(de_dist_inter_pupilar);
        Atributo de_sinal = new Atributo("de_sinal", entity.getDe_sinal());
        atributos.add(de_sinal);
        Atributo de_od_cilindro1 = new Atributo("de_od_cilindro1", entity.getDe_od_cilindro1());
        atributos.add(de_od_cilindro1);
        Atributo de_od_Cilindro2 = new Atributo("de_od_Cilindro2", entity.getDe_od_cilindro2());
        atributos.add(de_od_Cilindro2);
        Atributo de_od_cilindro3 = new Atributo("de_od_cilindro3", entity.getDe_od_cilindro3());
        atributos.add(de_od_cilindro3);
        Atributo de_od_eixo1 = new Atributo("de_od_eixo1", entity.getDe_od_eixo1());
        atributos.add(de_od_eixo1);
        Atributo de_od_eixo2 = new Atributo("de_od_eixo2", entity.getDe_od_eixo2());
        atributos.add(de_od_eixo2);
        Atributo de_od_eixo3 = new Atributo("de_od_eixo3", entity.getDe_od_eixo3());
        atributos.add(de_od_eixo3);
        Atributo de_od_erro_esferico = new Atributo("de_od_erro_esferico", entity.getDe_od_erro_esferico());
        atributos.add(de_od_erro_esferico);
        Atributo de_od_esferico1 = new Atributo("de_od_esferico1", entity.getDe_od_esferico1());
        atributos.add(de_od_esferico1);
        Atributo de_od_esferico2 = new Atributo("de_od_esferico2", entity.getDe_od_esferico2());
        atributos.add(de_od_esferico2);
        Atributo de_od_esferico3 = new Atributo("de_od_esferico3", entity.getDe_od_esferico3());
        atributos.add(de_od_esferico3);
        Atributo de_oe_cilindro1 = new Atributo("de_oe_cilindro1", entity.getDe_oe_cilindro1());
        atributos.add(de_oe_cilindro1);
        Atributo de_oe_cilindro2 = new Atributo("de_oe_cilindro2", entity.getDe_oe_cilindro2());
        atributos.add(de_oe_cilindro2);
        Atributo de_oe_cilindro3 = new Atributo("de_oe_cilindro3", entity.getDe_oe_cilindro3());
        atributos.add(de_oe_cilindro3);
        Atributo de_oe_eixo1 = new Atributo("de_oe_eixo1", entity.getDe_oe_eixo1());
        atributos.add(de_oe_eixo1);
        Atributo de_oe_eixo2 = new Atributo("de_oe_eixo2", entity.getDe_oe_eixo2());
        atributos.add(de_oe_eixo2);
        Atributo de_oe_eixo3 = new Atributo("de_oe_eixo3", entity.getDe_oe_eixo3());
        atributos.add(de_oe_eixo3);
        Atributo de_oe_erro_esferico = new Atributo("de_oe_erro_esferico", entity.getDe_oe_erro_esferico());
        atributos.add(de_oe_erro_esferico);
        Atributo de_oe_esferico1 = new Atributo("de_oe_esferico1", entity.getDe_oe_esferico1());
        atributos.add(de_oe_esferico1);
        Atributo de_oe_esferico2 = new Atributo("de_oe_esferico2", entity.getDe_oe_esferico2());
        atributos.add(de_oe_esferico2);
        Atributo de_oe_esferico3 = new Atributo("de_oe_esferico3", entity.getDe_oe_esferico3());
        atributos.add(de_oe_esferico3);
        Atributo fl_od_check = new Atributo("fl_od_check", entity.getFl_oe_check() + "");
        atributos.add(fl_od_check);
        Atributo de_cer_od1 = new Atributo("de_cer_od1", entity.getDe_cer_od1());
        atributos.add(de_cer_od1);
        Atributo de_cer_od2 = new Atributo("de_cer_od2", entity.getDe_cer_od2());
        atributos.add(de_cer_od2);
        Atributo de_cer_oe1 = new Atributo("de_cer_oe1", entity.getDe_cer_oe1());
        atributos.add(de_cer_oe1);
        Atributo de_cer_oe2 = new Atributo("de_cer_oe2", entity.getDe_cer_oe2());
        atributos.add(de_cer_oe2);
        Atributo de_para_perto_oe = new Atributo("de_para_perto_oe", entity.getDe_para_perto_oe());
        atributos.add(de_para_perto_oe);
        Atributo de_para_perto_od = new Atributo("de_para_perto_od", entity.getDe_para_perto_od());
        atributos.add(de_para_perto_od);
        Atributo de_dp = new Atributo("de_dp", entity.getDe_dp());
        atributos.add(de_dp);
        Atributo de_dnpod = new Atributo("de_dnpod", entity.getDe_dnpod());
        atributos.add(de_dnpod);
        Atributo de_sob_ciclo_oe = new Atributo("de_sob_ciclo_oe", entity.getDe_sob_ciclo_oe());
        atributos.add(de_sob_ciclo_oe);
        Atributo de_sob_ciclo_od = new Atributo("de_sob_ciclo_od", entity.getDe_sob_ciclo_od());
        atributos.add(de_sob_ciclo_od);
        Atributo bl_retorno = new Atributo("bl_retorno", entity.isBl_retorno() + "");
        atributos.add(bl_retorno);
        Atributo de_diagnostico = new Atributo("de_diagnostico", entity.getDe_diagnostico());
        atributos.add(de_diagnostico);
        Atributo de_conduta = new Atributo("de_conduta", entity.getDe_conduta());
        atributos.add(de_conduta);
        Atributo de_anamnese = new Atributo("de_anamnese", entity.getDe_anamnese());
        atributos.add(de_anamnese);
        Atributo de_biomicroscopia = new Atributo("de_biomicroscopia", entity.getDe_biomicroscopia());
        atributos.add(de_biomicroscopia);
        Atributo de_oftalmoscopia = new Atributo("de_oftalmoscopia", entity.getDe_oftalmoscopia());
        atributos.add(de_oftalmoscopia);
        Atributo de_obs = new Atributo("de_obs", entity.getDe_obs());
        atributos.add(de_obs);
        Atributo de_diag_extra = new Atributo("de_diag_extra", entity.getDe_diag_extra());
        atributos.add(de_diag_extra);
        Atributo de_convenio = new Atributo("de_convenio", entity.getDe_convenio());
        atributos.add(de_convenio);
        Atributo de_matricula = new Atributo("de_matricula", entity.getDe_matricula());
        atributos.add(de_matricula);
        Atributo de_dnpoe = new Atributo("de_dnpoe", entity.getDe_dnpoe());
        atributos.add(de_dnpoe);
        Atributo de_avodsc = new Atributo("de_avodsc", entity.getDe_avodsc());
        atributos.add(de_avodsc);
        Atributo de_avoesc = new Atributo("de_avoesc", entity.getDe_avoesc());
        atributos.add(de_avoesc);
        Atributo de_avodcc = new Atributo("de_avodcc", entity.getDe_avodcc());
        atributos.add(de_avodcc);
        Atributo de_avoecc = new Atributo("de_avoecc", entity.getDe_avoecc());
        atributos.add(de_avoecc);
        Atributo de_adicao = new Atributo("de_adicao", entity.getDe_adicao());
        atributos.add(de_adicao);
        Atributo de_para_longe_od_esf = new Atributo("de_para_longe_od_esf", entity.getDe_para_longe_od_esf());
        atributos.add(de_para_longe_od_esf);
        Atributo de_para_longe_od_cil = new Atributo("de_para_longe_od_cil", entity.getDe_para_longe_od_cil());
        atributos.add(de_para_longe_od_cil);
        Atributo de_para_longe_od_eixo = new Atributo("de_para_longe_od_eixo", entity.getDe_para_longe_od_eixo());
        atributos.add(de_para_longe_od_eixo);
        Atributo de_para_longe_oe_esf = new Atributo("de_para_longe_oe_esf", entity.getDe_para_longe_oe_esf());
        atributos.add(de_para_longe_oe_esf);
        Atributo de_para_longe_oe_cil = new Atributo("de_para_longe_oe_cil", entity.getDe_para_longe_oe_cil());
        atributos.add(de_para_longe_oe_cil);
        Atributo de_para_perto_oe_cil = new Atributo("de_para_perto_oe_cil", entity.getDe_para_perto_oe_cil());
        atributos.add(de_para_perto_oe_cil);
        Atributo de_para_longe_oe_eixo = new Atributo("de_para_longe_oe_eixo", entity.getDe_para_longe_oe_eixo());
        atributos.add(de_para_longe_oe_eixo);
        Atributo de_para_perto_od_cil = new Atributo("de_para_perto_od_cil", entity.getDe_para_perto_od_cil());
        atributos.add(de_para_perto_od_cil);
        Atributo de_para_perto_od_esf = new Atributo("de_para_perto_od_esf", entity.getDe_para_perto_od_esf());
        atributos.add(de_para_perto_od_esf);
        Atributo de_para_perto_od_eixo = new Atributo("de_para_perto_od_eixo", entity.getDe_para_perto_od_eixo());
        atributos.add(de_para_perto_od_eixo);
        Atributo de_para_perto_oe_esf = new Atributo("de_para_perto_oe_esf", entity.getDe_para_perto_oe_esf());
        atributos.add(de_para_perto_oe_esf);
        Atributo de_para_perto_oe_eixo = new Atributo("de_para_perto_oe_eixo", entity.getDe_para_perto_oe_eixo());
        atributos.add(de_para_perto_oe_eixo);
        Atributo de_sob_ciclo_oe_esf = new Atributo("de_sob_ciclo_oe_esf", entity.getDe_sob_ciclo_oe_esf());
        atributos.add(de_sob_ciclo_oe_esf);
        Atributo de_sob_ciclo_oe_cil = new Atributo("de_sob_ciclo_oe_cil", entity.getDe_sob_ciclo_oe_cil());
        atributos.add(de_sob_ciclo_oe_cil);
        Atributo de_sob_ciclo_oe_eixo = new Atributo("de_sob_ciclo_oe_eixo", entity.getDe_sob_ciclo_oe_eixo());
        atributos.add(de_sob_ciclo_oe_eixo);
        Atributo de_sob_ciclo_od_esf = new Atributo("de_sob_ciclo_od_esf", entity.getDe_sob_ciclo_od_esf());
        atributos.add(de_sob_ciclo_od_esf);
        Atributo de_sob_ciclo_od_cil = new Atributo("de_sob_ciclo_od_cil", entity.getDe_sob_ciclo_od_cil());
        atributos.add(de_sob_ciclo_od_cil);
        Atributo de_sob_ciclo_od_eixo = new Atributo("de_sob_ciclo_od_eixo", entity.getDe_sob_ciclo_od_eixo());
        atributos.add(de_sob_ciclo_od_eixo);
        Atributo de_ceratoscopia_computadorizada_OD_K2 = new Atributo("de_ceratoscopia_computadorizada_OD_K2", entity.getDe_ceratoscopia_computadorizada_OD_K2() + "");
        atributos.add(de_ceratoscopia_computadorizada_OD_K2);
        Atributo de_ceratoscopia_computadorizada_OE_K1 = new Atributo("de_ceratoscopia_computadorizada_OE_K1", entity.getDe_ceratoscopia_computadorizada_OE_K1() + "");
        atributos.add(de_ceratoscopia_computadorizada_OE_K1);
        Atributo de_ceratoscopia_computadorizada_OD_K1 = new Atributo("de_ceratoscopia_computadorizada_OD_K1", entity.getDe_ceratoscopia_computadorizada_OD_K1() + "");
        atributos.add(de_ceratoscopia_computadorizada_OD_K1);
        Atributo de_ceratoscopia_computadorizada_OD_DK = new Atributo("de_ceratoscopia_computadorizada_OD_DK", entity.getDe_ceratoscopia_computadorizada_OD_DK() + "");
        atributos.add(de_ceratoscopia_computadorizada_OD_DK);
        Atributo in_paquemetria_US_OD_central = new Atributo("in_paquemetria_US_OD_central", entity.getIn_paquemetria_US_OD_central() + "");
        atributos.add(in_paquemetria_US_OD_central);
        Atributo in_paquimetria_od_US1 = new Atributo("in_paquimetria_od_US1", entity.getIn_paquimetria_od_US1() + "");
        atributos.add(in_paquimetria_od_US1);
        Atributo in_paquimetria_od_US2 = new Atributo("in_paquimetria_od_US2", entity.getIn_paquimetria_od_US2() + "");
        atributos.add(in_paquimetria_od_US2);
        Atributo in_paquimetria_od_US3 = new Atributo("in_paquimetria_od_US3", entity.getIn_paquimetria_od_US3() + "");
        atributos.add(in_paquimetria_od_US3);
        Atributo in_paquimetria_od_US4 = new Atributo("in_paquimetria_od_US4", entity.getIn_paquimetria_od_US4() + "");
        atributos.add(in_paquimetria_od_US4);
        Atributo in_paquimetria_od_US5 = new Atributo("in_paquimetria_od_US5", entity.getIn_paquimetria_od_US5() + "");
        atributos.add(in_paquimetria_od_US5);
        Atributo in_paquimetria_oe_US1 = new Atributo("in_paquimetria_oe_US1", entity.getIn_paquimetria_oe_US1() + "");
        atributos.add(in_paquimetria_oe_US1);
        Atributo in_paquimetria_oe_US2 = new Atributo("in_paquimetria_oe_US2", entity.getIn_paquimetria_oe_US2() + "");
        atributos.add(in_paquimetria_oe_US2);
        Atributo in_paquimetria_oe_US3 = new Atributo("in_paquimetria_oe_US3", entity.getIn_paquimetria_oe_US3() + "");
        atributos.add(in_paquimetria_oe_US3);
        Atributo in_paquimetria_oe_US4 = new Atributo("in_paquimetria_oe_US4", entity.getIn_paquimetria_oe_US4() + "");
        atributos.add(in_paquimetria_oe_US4);
        Atributo in_paquimetria_oe_US5 = new Atributo("in_paquimetria_oe_US5", entity.getIn_paquimetria_oe_US5() + "");
        atributos.add(in_paquimetria_oe_US5);
        Atributo fl_lio_cons = new Atributo("fl_lio_cons", entity.getFl_lio_cons() + "");
        atributos.add(fl_lio_cons);
        Atributo de_lio_formula = new Atributo("de_lio_formula", entity.getDe_lio_formula() + "");
        atributos.add(de_lio_formula);
        Atributo fl_iol_od_d1 = new Atributo("fl_iol_od_d1", entity.getFl_iol_od_d1() + "");
        atributos.add(fl_iol_od_d1);
        Atributo fl_iol_od_d2 = new Atributo("fl_iol_od_d2", entity.getFl_iol_od_d2() + "");
        atributos.add(fl_iol_od_d2);
        Atributo fl_iol_od_d3 = new Atributo("fl_iol_od_d3", entity.getFl_iol_od_d3() + "");
        atributos.add(fl_iol_od_d3);
        Atributo fl_iol_od_d4 = new Atributo("fl_iol_od_d4", entity.getFl_iol_od_d4() + "");
        atributos.add(fl_iol_od_d4);
        Atributo fl_iol_od_d5 = new Atributo("fl_iol_od_d5", entity.getFl_iol_od_d5() + "");
        atributos.add(fl_iol_od_d5);
        Atributo fl_iol_od_d6 = new Atributo("fl_iol_od_d6", entity.getFl_iol_od_d6() + "");
        atributos.add(fl_iol_od_d6);
        Atributo fl_iol_od_d7 = new Atributo("fl_iol_od_d7", entity.getFl_iol_od_d7() + "");
        atributos.add(fl_iol_od_d7);
        Atributo fl_ref_od_d1 = new Atributo("fl_ref_od_d1", entity.getFl_ref_od_d1() + "");
        atributos.add(fl_ref_od_d1);
        Atributo fl_ref_od_d2 = new Atributo("fl_ref_od_d2", entity.getFl_ref_od_d2() + "");
        atributos.add(fl_ref_od_d2);
        Atributo fl_ref_od_d3 = new Atributo("fl_ref_od_d3", entity.getFl_ref_od_d3() + "");
        atributos.add(fl_ref_od_d3);
        Atributo fl_ref_od_d4 = new Atributo("fl_ref_od_d4", entity.getFl_ref_od_d4() + "");
        atributos.add(fl_ref_od_d4);
        Atributo fl_ref_od_d5 = new Atributo("fl_ref_od_d5", entity.getFl_ref_od_d5() + "");
        atributos.add(fl_ref_od_d5);
        Atributo fl_ref_od_d6 = new Atributo("fl_ref_od_d6", entity.getFl_ref_od_d6() + "");
        atributos.add(fl_ref_od_d6);
        Atributo fl_ref_od_d7 = new Atributo("fl_ref_od_d7", entity.getFl_ref_od_d7() + "");
        atributos.add(fl_ref_od_d7);
        Atributo fl_iol_oe_d1 = new Atributo("fl_iol_oe_d1", entity.getFl_iol_oe_d1() + "");
        atributos.add(fl_iol_oe_d1);
        Atributo fl_iol_oe_d2 = new Atributo("fl_iol_oe_d2", entity.getFl_iol_oe_d2() + "");
        atributos.add(fl_iol_oe_d2);
        Atributo fl_iol_oe_d3 = new Atributo("fl_iol_oe_d3", entity.getFl_iol_oe_d3() + "");
        atributos.add(fl_iol_oe_d3);
        Atributo fl_iol_oe_d4 = new Atributo("fl_iol_oe_d4", entity.getFl_iol_oe_d4() + "");
        atributos.add(fl_iol_oe_d4);
        Atributo fl_iol_oe_d5 = new Atributo("fl_iol_oe_d5", entity.getFl_iol_oe_d5() + "");
        atributos.add(fl_iol_oe_d5);
        Atributo fl_iol_oe_d6 = new Atributo("fl_iol_oe_d6", entity.getFl_iol_oe_d6() + "");
        atributos.add(fl_iol_oe_d6);
        Atributo fl_iol_oe_d7 = new Atributo("fl_iol_oe_d7", entity.getFl_iol_oe_d7() + "");
        atributos.add(fl_iol_oe_d7);
        Atributo fl_ref_oe_d1 = new Atributo("fl_ref_oe_d1", entity.getFl_ref_oe_d1() + "");
        atributos.add(fl_ref_oe_d1);
        Atributo fl_ref_oe_d2 = new Atributo("fl_ref_oe_d2", entity.getFl_ref_oe_d2() + "");
        atributos.add(fl_ref_oe_d2);
        Atributo fl_ref_oe_d3 = new Atributo("fl_ref_oe_d3", entity.getFl_ref_oe_d3() + "");
        atributos.add(fl_ref_oe_d3);
        Atributo fl_ref_oe_d4 = new Atributo("fl_ref_oe_d4", entity.getFl_ref_oe_d4() + "");
        atributos.add(fl_ref_oe_d4);
        Atributo fl_ref_oe_d5 = new Atributo("fl_ref_oe_d5", entity.getFl_ref_oe_d5() + "");
        atributos.add(fl_ref_oe_d5);
        Atributo fl_ref_oe_d6 = new Atributo("fl_ref_oe_d6", entity.getFl_ref_oe_d6() + "");
        atributos.add(fl_ref_oe_d6);
        Atributo fl_ref_oe_d7 = new Atributo("fl_ref_oe_d7", entity.getFl_ref_oe_d7() + "");
        atributos.add(fl_ref_oe_d7);
        Atributo fl_oe_check = new Atributo("fl_oe_check", entity.getFl_oe_check() + "");
        atributos.add(fl_oe_check);
        Atributo de_ceratoscopia_computadorizada_OD_DC = new Atributo("de_ceratoscopia_computadorizada_OD_DC", entity.getDe_ceratoscopia_computadorizada_OD_DC());
        atributos.add(de_ceratoscopia_computadorizada_OD_DC);
        Atributo de_ceratoscopia_computadorizada_OE_DC = new Atributo("de_ceratoscopia_computadorizada_OE_DC", entity.getDe_ceratoscopia_computadorizada_OE_DC());
        atributos.add(de_ceratoscopia_computadorizada_OE_DC);
        Atributo de_ceratoscopia_computadorizada_OD_RC = new Atributo("de_ceratoscopia_computadorizada_OD_RC", entity.getDe_ceratoscopia_computadorizada_OD_RC());
        atributos.add(de_ceratoscopia_computadorizada_OD_RC);
        Atributo de_ceratoscopia_computadorizada_OE_RC = new Atributo("de_ceratoscopia_computadorizada_OE_RC", entity.getDe_ceratoscopia_computadorizada_OE_RC());
        atributos.add(de_ceratoscopia_computadorizada_OE_RC);
        Atributo de_ceratoscopia_computadorizada_OD_DM = new Atributo("de_ceratoscopia_computadorizada_OD_DM", entity.getDe_ceratoscopia_computadorizada_OD_DM());
        atributos.add(de_ceratoscopia_computadorizada_OD_DM);
        Atributo de_ceratoscopia_computadorizada_OE_DM = new Atributo("de_ceratoscopia_computadorizada_OE_DM", entity.getDe_ceratoscopia_computadorizada_OE_DM());
        atributos.add(de_ceratoscopia_computadorizada_OE_DM);
        Atributo de_ceratoscopia_computadorizada_OE_K2 = new Atributo("de_ceratoscopia_computadorizada_OE_K2", entity.getDe_ceratoscopia_computadorizada_OE_K2());
        atributos.add(de_ceratoscopia_computadorizada_OE_K2);
        Atributo de_ceratoscopia_computadorizada_OE_DK = new Atributo("de_ceratoscopia_computadorizada_OE_DK", entity.getDe_ceratoscopia_computadorizada_OE_DK());
        atributos.add(de_ceratoscopia_computadorizada_OE_DK);
        Atributo in_microscopia_densidade_endotelial_OD = new Atributo("in_microscopia_densidade_endotelial_OD", entity.getIn_microscopia_densidade_endotelial_OD() + "");
        atributos.add(in_microscopia_densidade_endotelial_OD);
        Atributo in_microscopia_densidade_endotelial_OE = new Atributo("in_microscopia_densidade_endotelial_OE", entity.getIn_microscopia_densidade_endotelial_OE() + "");
        atributos.add(in_microscopia_densidade_endotelial_OE);
        Atributo de_microscopia_pleomorfismo_OD = new Atributo("de_microscopia_pleomorfismo_OD", entity.getDe_microscopia_pleomorfismo_OD());
        atributos.add(de_microscopia_pleomorfismo_OD);
        Atributo de_microscopia_pleomorfismo_OE = new Atributo("de_microscopia_pleomorfismo_OE", entity.getDe_microscopia_pleomorfismo_OE());
        atributos.add(de_microscopia_pleomorfismo_OE);
        Atributo de_microscopia_polimegatismo_OD = new Atributo("de_microscopia_polimegatismo_OD", entity.getDe_microscopia_polimegatismo_OD());
        atributos.add(de_microscopia_polimegatismo_OD);
        Atributo de_microscopia_polimegatismo_OE = new Atributo("de_microscopia_polimegatismo_OE", entity.getDe_microscopia_polimegatismo_OE());
        atributos.add(de_microscopia_polimegatismo_OE);
        Atributo de_microscopia_limites_celulares_OD = new Atributo("de_microscopia_limites_celulares_OD", entity.getDe_microscopia_limites_celulares_OD());
        atributos.add(de_microscopia_limites_celulares_OD);
        Atributo de_microscopia_limites_celulares_OE = new Atributo("de_microscopia_limites_celulares_OE", entity.getDe_microscopia_limites_celulares_OE());
        atributos.add(de_microscopia_limites_celulares_OE);
        Atributo de_lio_oe_k1 = new Atributo("fl_lio_oe_k1", entity.getFl_lio_oe_k1() + "");
        atributos.add(de_lio_oe_k1);
        Atributo de_lio_oe_k2 = new Atributo("fl_lio_oe_k2", entity.getFl_lio_oe_k2() + "");
        atributos.add(de_lio_oe_k2);
        Atributo de_lio_od_k1 = new Atributo("fl_lio_od_k1", entity.getFl_lio_od_k1() + "");
        atributos.add(de_lio_od_k1);
        Atributo de_lio_od_k2 = new Atributo("fl_lio_od_k2", entity.getFl_lio_od_k2() + "");
        atributos.add(de_lio_od_k2);
        return insertDados("tbl_consulta", atributos);
    }

    public ConsultaEntity findById(String auxId) {
        return null;
    }

    public ArrayList listConsultaByProntuario(String auxIntervalo1, String auxIntervalo2) {
        return null;
    }

    public ArrayList listDataByProntuario(int prontuario, int fk_medico) {
        ArrayList lista = null;
        String condicao = null;
        try {
            System.out.println("select dt_data from tbl_consulta, tbl_cliente where tbl_consulta.fk_prontuario = " + prontuario + " and tbl_cliente.id_prontuario = " + prontuario + " and tbl_consulta.fk_medico = " + fk_medico + " and tbl_cliente.fk_medico = " + fk_medico);
            ResultSet rs = executaConsulta("select dt_data from tbl_consulta, tbl_cliente where tbl_consulta.fk_prontuario = " + prontuario + " and tbl_cliente.id_prontuario = " + prontuario + " and tbl_consulta.fk_medico = " + fk_medico + " and tbl_cliente.fk_medico = " + fk_medico);
            if (rs != null) {
                lista = new ArrayList();
                while (rs.next()) {
                    Locale locale = Locale.getDefault();
                    String dt_data_aux = null;
                    try {
                        DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM, locale);
                        dt_data_aux = df.format(rs.getDate("dt_data"));
                    } catch (Exception ex) {
                        dt_data_aux = "";
                    }
                    lista.add(dt_data_aux);
                }
            }
        } catch (SQLException ex) {
        }
        return lista;
    }

    public ConsultaEntityNovo findByProntuarioData(String prontuario, String data, int fk_medico) {
        Data d = new Data();
        data = HCalendar.getStringyyyymmdd(HCalendar.setData(data));
        ConsultaEntityNovo entity = new ConsultaEntityNovo();
        try {
            System.out.println("select * from tbl_consulta where fk_prontuario = " + prontuario + " and dt_data = '" + data + "'");
            ResultSet rs = executaConsulta("select * from tbl_consulta where fk_prontuario = " + prontuario + " and dt_data = '" + data + "'");
            if (rs != null) {
                while (rs.next()) {
                    entity.setId_consulta(rs.getInt("id_consulta"));
                    entity.setFk_medico(rs.getInt("fk_medico"));
                    entity.setFk_prontuario(rs.getInt("fk_prontuario"));
                    entity.setDt_data(HCalendar.setData(rs.getString("dt_data")));
                    entity.setDe_pupilas(rs.getString("de_pupilas"));
                    entity.setDe_palpebras(rs.getString("de_palpebras"));
                    entity.setDe_conjuntivas(rs.getString("de_conjuntivas"));
                    entity.setDe_aparelho_lacrimal(rs.getString("de_aparelho_lacrimal"));
                    entity.setDe_cover_test(rs.getString("de_cover_test"));
                    entity.setDe_hirschberg(rs.getString("de_hirschberg"));
                    entity.setDe_ppc(rs.getString("de_ppc"));
                    entity.setIn_rot_od1(rs.getInt("in_rot_od1"));
                    entity.setIn_rot_od2(rs.getInt("in_rot_od2"));
                    entity.setIn_rot_od3(rs.getInt("in_rot_od3"));
                    entity.setIn_rot_od4(rs.getInt("in_rot_od4"));
                    entity.setIn_rot_od5(rs.getInt("in_rot_od5"));
                    entity.setIn_rot_od6(rs.getInt("in_rot_od6"));
                    entity.setIn_rot_oe1(rs.getInt("in_rot_oe1"));
                    entity.setIn_rot_oe2(rs.getInt("in_rot_oe2"));
                    entity.setIn_rot_oe3(rs.getInt("in_rot_oe3"));
                    entity.setIn_rot_oe4(rs.getInt("in_rot_oe4"));
                    entity.setIn_rot_oe5(rs.getInt("in_rot_oe5"));
                    entity.setIn_rot_oe6(rs.getInt("in_rot_oe6"));
                    entity.setIn_tonometria_aplanacao_oe(rs.getInt("in_tonometria_aplanacao_oe"));
                    entity.setIn_tonometria_aplanacao_od(rs.getInt("in_tonometria_aplanacao_od"));
                    entity.setIn_tonometria_pneumatica_oe(rs.getInt("in_tonometria_pneumatica_oe"));
                    entity.setIn_tonometria_pneumatica_od(rs.getInt("in_tonometria_pneumatica_od"));
                    entity.setDe_tonometria_pneumatica_hora(rs.getString("de_tonometria_pneumatica_hora"));
                    entity.setDt_auto_data(HCalendar.setData(rs.getString("dt_auto_data")));
                    entity.setDe_auto_hora(rs.getString("de_auto_hora"));
                    entity.setDe_auto_dist_vertice(rs.getString("de_auto_dist_vertice"));
                    entity.setDe_dist_inter_pupilar(rs.getString("de_dist_inter_pupilar"));
                    entity.setDe_sinal(rs.getString("de_sinal"));
                    entity.setDe_od_cilindro1(rs.getString("de_od_cilindro1"));
                    entity.setDe_od_cilindro2(rs.getString("de_od_cilindro2"));
                    entity.setDe_od_cilindro3(rs.getString("de_od_cilindro3"));
                    entity.setDe_od_eixo1(rs.getString("de_od_eixo1"));
                    entity.setDe_od_eixo2(rs.getString("de_od_eixo2"));
                    entity.setDe_od_eixo3(rs.getString("de_od_eixo3"));
                    entity.setDe_od_erro_esferico(rs.getString("de_od_erro_esferico"));
                    entity.setDe_od_esferico1(rs.getString("de_od_esferico1"));
                    entity.setDe_od_esferico2(rs.getString("de_od_esferico2"));
                    entity.setDe_od_esferico3(rs.getString("de_od_esferico3"));
                    entity.setDe_oe_cilindro1(rs.getString("de_oe_cilindro1"));
                    entity.setDe_oe_cilindro2(rs.getString("de_oe_cilindro2"));
                    entity.setDe_oe_cilindro3(rs.getString("de_oe_cilindro3"));
                    entity.setDe_oe_eixo1(rs.getString("de_oe_eixo1"));
                    entity.setDe_oe_eixo2(rs.getString("de_oe_eixo2"));
                    entity.setDe_oe_eixo3(rs.getString("de_oe_eixo3"));
                    entity.setDe_oe_erro_esferico(rs.getString("de_oe_erro_esferico"));
                    entity.setDe_oe_esferico1(rs.getString("de_oe_esferico1"));
                    entity.setDe_oe_esferico2(rs.getString("de_oe_esferico2"));
                    entity.setDe_oe_esferico3(rs.getString("de_oe_esferico3"));
                    entity.setFl_od_check(rs.getFloat("fl_od_check"));
                    entity.setDe_cer_od1(rs.getString("de_cer_od1"));
                    entity.setDe_cer_od2(rs.getString("de_cer_od2"));
                    entity.setDe_cer_oe1(rs.getString("de_cer_oe1"));
                    entity.setDe_cer_oe2(rs.getString("de_cer_oe2"));
                    entity.setDe_para_perto_oe(rs.getString("de_para_perto_oe"));
                    entity.setDe_para_perto_od(rs.getString("de_para_perto_od"));
                    entity.setDe_dp(rs.getString("de_dp"));
                    entity.setDe_dnpod(rs.getString("de_dnpod"));
                    entity.setDe_sob_ciclo_oe(rs.getString("de_sob_ciclo_oe"));
                    entity.setDe_sob_ciclo_od(rs.getString("de_sob_ciclo_od"));
                    entity.setDe_diagnostico(rs.getString("de_diagnostico"));
                    entity.setDe_conduta(rs.getString("de_conduta"));
                    entity.setDe_anamnese(rs.getString("de_anamnese"));
                    entity.setDe_biomicroscopia(rs.getString("de_biomicroscopia"));
                    entity.setDe_oftalmoscopia(rs.getString("de_oftalmoscopia"));
                    entity.setDe_obs(rs.getString("de_obs"));
                    entity.setDe_diag_extra(rs.getString("de_diag_extra"));
                    entity.setDe_convenio(rs.getString("de_convenio"));
                    entity.setDe_matricula(rs.getString("de_matricula"));
                    entity.setDe_dnpoe(rs.getString("de_dnpoe"));
                    entity.setDe_avodsc(rs.getString("de_avodsc"));
                    entity.setDe_avoesc(rs.getString("de_avoesc"));
                    entity.setDe_avodcc(rs.getString("de_avodcc"));
                    entity.setDe_avoecc(rs.getString("de_avoecc"));
                    entity.setDe_adicao(rs.getString("de_adicao"));
                    entity.setDe_para_longe_od_esf(rs.getString("de_para_longe_od_esf"));
                    entity.setDe_para_longe_od_cil(rs.getString("de_para_longe_od_cil"));
                    entity.setDe_para_longe_od_eixo(rs.getString("de_para_longe_od_eixo"));
                    entity.setDe_para_longe_oe_esf(rs.getString("de_para_longe_oe_esf"));
                    entity.setDe_para_longe_oe_cil(rs.getString("de_para_longe_oe_cil"));
                    entity.setDe_para_perto_oe_cil(rs.getString("de_para_perto_oe_cil"));
                    entity.setDe_para_longe_oe_eixo(rs.getString("de_para_longe_oe_eixo"));
                    entity.setDe_para_perto_od_cil(rs.getString("de_para_perto_od_cil"));
                    entity.setDe_para_perto_od_esf(rs.getString("de_para_perto_od_esf"));
                    entity.setDe_para_perto_od_eixo(rs.getString("de_para_perto_od_eixo"));
                    entity.setDe_para_perto_oe_esf(rs.getString("de_para_perto_oe_esf"));
                    entity.setDe_para_perto_oe_eixo(rs.getString("de_para_perto_oe_eixo"));
                    entity.setDe_sob_ciclo_oe_esf(rs.getString("de_sob_ciclo_oe_esf"));
                    entity.setDe_sob_ciclo_oe_cil(rs.getString("de_sob_ciclo_oe_cil"));
                    entity.setDe_sob_ciclo_oe_eixo(rs.getString("de_sob_ciclo_oe_eixo"));
                    entity.setDe_sob_ciclo_od_esf(rs.getString("de_sob_ciclo_od_esf"));
                    entity.setDe_sob_ciclo_od_cil(rs.getString("de_sob_ciclo_od_cil"));
                    entity.setDe_sob_ciclo_od_eixo(rs.getString("de_sob_ciclo_od_eixo"));
                    entity.setDe_ceratoscopia_computadorizada_OD_K2(rs.getString("de_ceratoscopia_computadorizada_OD_K2"));
                    entity.setDe_ceratoscopia_computadorizada_OE_K1(rs.getString("de_ceratoscopia_computadorizada_OE_K1"));
                    entity.setDe_ceratoscopia_computadorizada_OD_K1(rs.getString("de_ceratoscopia_computadorizada_OD_K1"));
                    entity.setDe_ceratoscopia_computadorizada_OD_DK(rs.getString("de_ceratoscopia_computadorizada_OD_DK"));
                    entity.setIn_paquemetria_US_OD_central(rs.getInt("in_paquemetria_US_OD_central"));
                    entity.setIn_paquimetria_od_US1(rs.getInt("in_paquimetria_od_US1"));
                    entity.setIn_paquimetria_od_US2(rs.getInt("in_paquimetria_od_US2"));
                    entity.setIn_paquimetria_od_US3(rs.getInt("in_paquimetria_od_US3"));
                    entity.setIn_paquimetria_od_US4(rs.getInt("in_paquimetria_od_US4"));
                    entity.setIn_paquimetria_od_US5(rs.getInt("in_paquimetria_od_US5"));
                    entity.setFl_lio_cons(rs.getFloat("fl_lio_cons"));
                    entity.setFl_iol_od_d1(rs.getFloat("fl_iol_od_d1"));
                    entity.setFl_iol_od_d2(rs.getFloat("fl_iol_od_d2"));
                    entity.setFl_iol_od_d3(rs.getFloat("fl_iol_od_d3"));
                    entity.setFl_iol_od_d4(rs.getFloat("fl_iol_od_d4"));
                    entity.setFl_iol_od_d5(rs.getFloat("fl_iol_od_d5"));
                    entity.setFl_iol_od_d6(rs.getFloat("fl_iol_od_d6"));
                    entity.setFl_iol_od_d7(rs.getFloat("fl_iol_od_d7"));
                    entity.setFl_ref_od_d1(rs.getFloat("fl_ref_od_d1"));
                    entity.setFl_ref_od_d2(rs.getFloat("fl_ref_od_d2"));
                    entity.setFl_ref_od_d3(rs.getFloat("fl_ref_od_d3"));
                    entity.setFl_ref_od_d4(rs.getFloat("fl_ref_od_d4"));
                    entity.setFl_ref_od_d5(rs.getFloat("fl_ref_od_d5"));
                    entity.setFl_ref_od_d6(rs.getFloat("fl_ref_od_d6"));
                    entity.setFl_ref_od_d7(rs.getFloat("fl_ref_od_d7"));
                    entity.setFl_oe_check(rs.getFloat("fl_oe_check"));
                    entity.setDe_ceratoscopia_computadorizada_OD_DC(rs.getString("de_ceratoscopia_computadorizada_OD_DC"));
                    entity.setDe_ceratoscopia_computadorizada_OE_DC(rs.getString("de_ceratoscopia_computadorizada_OE_DC"));
                    entity.setDe_ceratoscopia_computadorizada_OD_RC(rs.getString("de_ceratoscopia_computadorizada_OD_RC"));
                    entity.setDe_ceratoscopia_computadorizada_OE_RC(rs.getString("de_ceratoscopia_computadorizada_OE_RC"));
                    entity.setDe_ceratoscopia_computadorizada_OD_DM(rs.getString("de_ceratoscopia_computadorizada_OD_DM"));
                    entity.setDe_ceratoscopia_computadorizada_OE_DM(rs.getString("de_ceratoscopia_computadorizada_OE_DM"));
                    entity.setDe_ceratoscopia_computadorizada_OE_K2(rs.getString("de_ceratoscopia_computadorizada_OE_K2"));
                    entity.setDe_ceratoscopia_computadorizada_OE_DK(rs.getString("de_ceratoscopia_computadorizada_OE_DK"));
                    entity.setIn_microscopia_densidade_endotelial_OD(rs.getInt("in_microscopia_densidade_endotelial_OD"));
                    entity.setIn_microscopia_densidade_endotelial_OE(rs.getInt("in_microscopia_densidade_endotelial_OE"));
                    entity.setDe_microscopia_pleomorfismo_OD(rs.getString("de_microscopia_pleomorfismo_OD"));
                    entity.setDe_microscopia_pleomorfismo_OE(rs.getString("de_microscopia_pleomorfismo_OE"));
                    entity.setDe_microscopia_polimegatismo_OD(rs.getString("de_microscopia_polimegatismo_OD"));
                    entity.setDe_microscopia_polimegatismo_OE(rs.getString("de_microscopia_polimegatismo_OE"));
                    entity.setDe_microscopia_limites_celulares_OD(rs.getString("de_microscopia_limites_celulares_OD"));
                    entity.setDe_microscopia_limites_celulares_OE(rs.getString("de_microscopia_limites_celulares_OE"));
                    entity.setFl_lio_od_k1(rs.getFloat("fl_lio_od_k1"));
                    entity.setFl_lio_od_k2(rs.getFloat("fl_lio_od_k2"));
                    entity.setDe_tonometria_aplanacao_hora(rs.getString("de_tonometria_aplanacao_hora"));
                    entity.setFl_lio_oe_k1(rs.getFloat("fl_lio_oe_k1"));
                    entity.setFl_lio_oe_k2(rs.getFloat("fl_lio_oe_k2"));
                    entity.setFl_iol_oe_d1(rs.getFloat("fl_iol_oe_d1"));
                    entity.setFl_iol_oe_d2(rs.getFloat("fl_iol_oe_d2"));
                    entity.setFl_iol_oe_d3(rs.getFloat("fl_iol_oe_d3"));
                    entity.setFl_iol_oe_d4(rs.getFloat("fl_iol_oe_d4"));
                    entity.setFl_iol_oe_d5(rs.getFloat("fl_iol_oe_d5"));
                    entity.setFl_iol_oe_d6(rs.getFloat("fl_iol_oe_d6"));
                    entity.setFl_iol_oe_d7(rs.getFloat("fl_iol_oe_d7"));
                    entity.setFl_ref_oe_d1(rs.getFloat("fl_ref_oe_d1"));
                    entity.setFl_ref_oe_d2(rs.getFloat("fl_ref_oe_d2"));
                    entity.setFl_ref_oe_d3(rs.getFloat("fl_ref_oe_d3"));
                    entity.setFl_ref_oe_d4(rs.getFloat("fl_ref_oe_d4"));
                    entity.setFl_ref_oe_d5(rs.getFloat("fl_ref_oe_d5"));
                    entity.setFl_ref_oe_d6(rs.getFloat("fl_ref_oe_d6"));
                    entity.setFl_ref_oe_d7(rs.getFloat("fl_ref_oe_d7"));
                    entity.setIn_paquimetria_oe_US1(rs.getInt("in_paquimetria_oe_US1"));
                    entity.setIn_paquimetria_oe_US2(rs.getInt("in_paquimetria_oe_US2"));
                    entity.setIn_paquimetria_oe_US3(rs.getInt("in_paquimetria_oe_US3"));
                    entity.setIn_paquimetria_oe_US4(rs.getInt("in_paquimetria_oe_US4"));
                    entity.setIn_paquimetria_oe_US5(rs.getInt("in_paquimetria_oe_US5"));
                    entity.setDe_lio_formula(rs.getString("de_lio_formula"));
                }
            }
        } catch (SQLException ex) {
        }
        return entity;
    }

    public ArrayList listHistoricoTonometria(int prontuario, int medico) {
        ConsultaEntityNovo entity;
        ArrayList lista = null;
        try {
            ResultSet rs = executaConsulta("select dt_data, in_tonometria_aplanacao_oe, in_tonometria_aplanacao_od, in_tonometria_pneumatica_oe, in_tonometria_pneumatica_od, de_auto_hora from tbl_consulta where fk_prontuario = " + prontuario + " and fk_medico = " + medico);
            System.out.println("select dt_data, in_tonometria_aplanacao_oe, in_tonometria_aplanacao_od, in_tonometria_pneumatica_oe, in_tonometria_pneumatica_od, de_auto_hora from tbl_consulta where fk_prontuario = " + prontuario + " and fk_medico = " + medico);
            if (rs != null) {
                lista = new ArrayList();
                while (rs.next()) {
                    entity = new ConsultaEntityNovo();
                    entity.setDt_data(HCalendar.setData(rs.getString("dt_data")));
                    entity.setIn_tonometria_aplanacao_oe(rs.getInt("in_tonometria_aplanacao_oe"));
                    entity.setIn_tonometria_aplanacao_od(rs.getInt("in_tonometria_aplanacao_od"));
                    entity.setIn_tonometria_pneumatica_oe(rs.getInt("in_tonometria_pneumatica_oe"));
                    entity.setIn_tonometria_pneumatica_od(rs.getInt("in_tonometria_pneumatica_od"));
                    entity.setDe_auto_hora(rs.getString("de_auto_hora"));
                    lista.add(entity);
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return lista;
    }

    public ArrayList listHistoricoRefracao(int prontuario, int medico) {
        ConsultaEntityNovo entity;
        ArrayList lista = null;
        try {
            ResultSet rs = executaConsulta("select dt_data, de_para_longe_od_esf, de_para_longe_od_cil, de_para_longe_od_eixo, de_para_longe_oe_esf, de_para_longe_oe_cil, de_para_longe_oe_eixo, de_para_perto_od_esf, de_para_perto_od_cil, de_para_perto_od_eixo, de_para_perto_oe_esf, de_para_perto_oe_cil, de_para_perto_oe_eixo from tbl_consulta where fk_prontuario = " + prontuario + " and fk_medico = " + medico);
            if (rs != null) {
                lista = new ArrayList();
                while (rs.next()) {
                    entity = new ConsultaEntityNovo();
                    entity.setDt_data(HCalendar.setData(rs.getString("dt_data")));
                    entity.setDe_para_longe_od_esf(rs.getString("de_para_longe_od_esf"));
                    entity.setDe_para_longe_od_cil(rs.getString("de_para_longe_od_cil"));
                    entity.setDe_para_longe_od_eixo(rs.getString("de_para_longe_od_eixo"));
                    entity.setDe_para_longe_oe_esf(rs.getString("de_para_longe_oe_esf"));
                    entity.setDe_para_longe_oe_cil(rs.getString("de_para_longe_oe_cil"));
                    entity.setDe_para_perto_oe_cil(rs.getString("de_para_perto_oe_cil"));
                    entity.setDe_para_longe_oe_eixo(rs.getString("de_para_longe_oe_eixo"));
                    entity.setDe_para_perto_od_cil(rs.getString("de_para_perto_od_cil"));
                    entity.setDe_para_perto_od_esf(rs.getString("de_para_perto_od_esf"));
                    entity.setDe_para_perto_od_eixo(rs.getString("de_para_perto_od_eixo"));
                    entity.setDe_para_perto_oe_esf(rs.getString("de_para_perto_oe_esf"));
                    entity.setDe_para_perto_oe_eixo(rs.getString("de_para_perto_oe_eixo"));
                    lista.add(entity);
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return lista;
    }
}
