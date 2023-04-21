/*
 * Criado em 04/01/2006
 *
 * TODO Para alterar o gabarito desse arquivo gerado, vá para
 * Janela - Preferências - Java - Estilo de Código - Gabaritos de Código
 */
package componente.consulta.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;



import pattern.data_access_object.MySQL;
import pattern.util.HCalendar;


/**
 * @author Henrick Daniel
 *
 * TODO Para alterar o gabarito desse comentário de tipo gerado, vá para
 * Janela - Preferências - Java - Estilo de Código - Gabaritos de Código
 */
public class ConsultaDAOODBC  extends MySQL implements ConsultaDAO{
	
	/**
	 * @param whichFactory
	 */
	public ConsultaDAOODBC() {
		super(2);
		// TODO Stub de construtor gerado automaticamente
	}

	public ConsultaEntity findById(String auxId){
		//		Variável de retorno
		   ConsultaEntity entity = null;
		 	
		   //Limpar o parâmetro de entrada
		   auxId = auxId.trim();
	        
		   try
		   {           
			   //Pesquisar registro
			   ResultSet rs = selectDados("Consulta"," Prontuário  = "+ auxId );
	       	   
			   if((rs!=null) && rs.next()) 
			   {
				   //Criar objeto de retorno
				   entity = new ConsultaEntity();
				   entity.setIn_Prontuário(rs.getInt("Prontuário"));
				   	entity.setDt_Data(HCalendar.setData(rs.getString("Data")));
				   	System.out.println("................"+entity.getDt_Data());
				   	entity.setDe_Pupilas(rs.getString("Pupilas"));
				   	entity.setDe_Pálpebras(rs.getString("Pálpebras"));
				   	System.out.println("pal >> "+rs.getString("Pálpebras"));
				   	entity.setDe_Conjuntivas(rs.getString("Conjuntivas"));
				   	entity.setDe_Aparelho_Lacrimal(rs.getString("Aparelho_Lacrimal"));
				   	entity.setDe_Cover_Test(rs.getString("Cover_Test"));
				   	entity.setDe_Hirschberg(rs.getString("Hirschberg"));
				   	entity.setDe_PPC(rs.getString("PPC"));
				   	entity.setIn_RotOdUm(rs.getInt("RotOdUm"));
				   	entity.setIn_RotOdDois(rs.getInt("RotOdDois"));
				   	entity.setIn_RotOdTres(rs.getInt("RotOdTres"));
				   	entity.setIn_RotOdQuatro(rs.getInt("RotOdQuatro"));
				   	entity.setIn_RotOdCinco(rs.getInt("RotOdCinco"));
				   	entity.setIn_RotOdSeis(rs.getInt("RotOdSeis"));
				   	entity.setIn_RotOeUm(rs.getInt("RotOeUm"));
				   	entity.setIn_RotOeDois(rs.getInt("RotOeDois"));
				   	entity.setIn_RotOeTres(rs.getInt("RotOeTres"));
				   	entity.setIn_RotOeQuatro(rs.getInt("RotOeQuatro"));
				   	entity.setIn_RotOeCinco(rs.getInt("RotOeCinco"));
				   	entity.setIn_RotOeSeis(rs.getInt("RotOeSeis"));
				   	entity.setDe_TBD_OD(rs.getString("TBD_OD"));
				   	entity.setDe_TBD_OE(rs.getString("TBD_OE"));
				   	entity.setDe_TBDHora(rs.getString("TBDHora"));
				   	entity.setIn_Tonometria_OE(rs.getInt("Tonometria_OE"));
				   	entity.setIn_Tonometria_OD(rs.getInt("Tonometria_OD"));
				   	entity.setDe_TonoHora(rs.getString("TonoHora"));
				   	entity.setDt_AutoData(HCalendar.setData(rs.getString("AutoData")));
				   	entity.setDe_AutoHora(rs.getString("AutoHora"));
				   	entity.setDe_AutoDistVertice(rs.getString("AutoDistVertice"));
				   	entity.setDe_DistInterPupilar(rs.getString("DistInterPupilar"));
				   	entity.setDe_Sinal(rs.getString("Sinal"));
				   	entity.setDe_ODCilindroUm(rs.getString("ODCilindroUm"));
				   	entity.setDe_ODCilindroDois(rs.getString("ODCilindroDois"));
				   	entity.setDe_ODCilindroTres(rs.getString("ODCilindroTres"));
				   	entity.setDe_ODEixoUm(rs.getString("ODEixoUm"));
				   	entity.setDe_ODEixoDois(rs.getString("ODEixoDois"));
				   	entity.setDe_ODEixoTres(rs.getString("ODEixoTres"));
				   	entity.setDe_ODErroEsferico(rs.getString("ODErroEsferico"));
				   	entity.setDe_ODEsfericoUm(rs.getString("ODEsfericoUm"));
				   	entity.setDe_ODEsfericoDois(rs.getString("ODEsfericoDois"));
				   	entity.setDe_ODEsfericoTres(rs.getString("ODEsfericoTres"));
				   	entity.setDe_OECilindroUm(rs.getString("OECilindroUm"));
				   	entity.setDe_OECilindroDois(rs.getString("OECilindroDois"));
				   	entity.setDe_OECilindroTres(rs.getString("OECilindroTres"));
				   	entity.setDe_OEEixoUm(rs.getString("OEEixoUm"));
				   	entity.setDe_OEEixoDois(rs.getString("OEEixoDois"));
				   	entity.setDe_OEEixoTres(rs.getString("OEEixoTres"));
				   	entity.setDe_OEErroEsferico(rs.getString("OEErroEsferico"));
				   	entity.setDe_OEEsfericoUm(rs.getString("OEEsfericoUm"));
				   	entity.setDe_OEEsfericoDois(rs.getString("OEEsfericoDois"));
				   	entity.setDe_OEEsfericoTres(rs.getString("OEEsfericoTres"));
				   	entity.setIn_ODCheck(rs.getInt("ODCheck"));
				   	entity.setIn_OECheck(rs.getInt("OECheck"));
				   	entity.setDe_CerOdUm(rs.getString("CerOdUm"));
				   	entity.setDe_CerOdDois(rs.getString("CerOdDois"));
				   	entity.setDe_CerOeUm(rs.getString("CerOeUm"));
				   	entity.setDe_CerOeDois(rs.getString("CerOeDois"));
				   	entity.setDe_RetOdUm(rs.getString("RetOdUm"));
				   	entity.setDe_RetOdDois(rs.getString("RetOdDois"));
				   	entity.setDe_RetOeUm(rs.getString("RetOeUm"));
				   	entity.setDe_RetOeDois(rs.getString("RetOeDois"));
				   	entity.setDe_Para_Longe_OE(rs.getString("Para_Longe_OE"));
				   	entity.setDe_Para_longe_OD(rs.getString("Para_longe_OD"));
				   	entity.setDe_Para_perto_OE(rs.getString("Para_perto_OE"));
				   	entity.setDe_Para_Perto_OD(rs.getString("Para_Perto_OD"));
				   	entity.setDe_DP(rs.getString("DP"));
				   	entity.setDe_DNP(rs.getString("DNP"));
				   	entity.setDe_Sob_ciclo_OE(rs.getString("Sob_ciclo_OE"));
				   	entity.setDe_Sob_ciclo_OD(rs.getString("Sob_ciclo_OD"));
				   	entity.setDe_Retorno(rs.getBoolean("Retorno"));
				   	entity.setDe_Diagnóstico(rs.getString("Diagnóstico"));
				   	entity.setDe_Conduta(rs.getString("Conduta"));
				   	entity.setDe_Anamnese(rs.getString("Anamnese"));
				   	entity.setDe_Biomicroscopia(rs.getString("Biomicroscopia"));
				   	entity.setDe_Oftalmoscopia(rs.getString("Oftalmoscopia"));
				   	entity.setDe_Obs(rs.getString("Obs"));
				   	entity.setDe_DiagExtra(rs.getString("DiagExtra"));
				   	entity.setDe_Convenio(rs.getString("Convenio"));
				   	entity.setDe_Matrícula(rs.getString("Matrícula"));
				   	entity.setIn_Flag(rs.getInt("Flag"));
				   	entity.setDe_DNPOE(rs.getString("DNPOE"));
				   	entity.setDe_AVODSC(rs.getString("AVODSC"));
				   	entity.setDe_AVOESC(rs.getString("AVOESC"));
				   	entity.setDe_AVODCC(rs.getString("AVODCC"));
				   	entity.setDe_AVOECC(rs.getString("AVOECC"));
				   	entity.setDe_Para_Longe_OD_Esf(rs.getString("Para_Longe_OD_Esf"));
				   	entity.setDe_Para_Longe_OD_Cil(rs.getString("Para_Longe_OD_Cil"));
				   	entity.setDe_Para_Longe_OD_Eixo(rs.getString("Para_Longe_OD_Eixo"));
				   	entity.setDe_Para_Longe_OE_Esf(rs.getString("Para_Longe_OE_Esf"));
				   	entity.setDe_Para_Longe_OE_Cil(rs.getString("Para_Longe_OE_Cil"));
				   	entity.setDe_Para_Longe_OE_Eixo(rs.getString("Para_Longe_OE_Eixo"));
				   	entity.setDe_Adicao(rs.getString("Adicao"));
				   	entity.setDe_Para_Perto_OD_Esf(rs.getString("Para_Perto_OD_Esf"));
				   	entity.setDe_Para_Perto_OD_Cil(rs.getString("Para_Perto_OD_Cil"));
				   	entity.setDe_Para_Perto_OD_Eixo(rs.getString("Para_Perto_OD_Eixo"));
				   	entity.setDe_Para_Perto_OE_Esf(rs.getString("Para_Perto_OE_Esf"));
				   	entity.setDe_Para_Perto_OE_Cil(rs.getString("Para_Perto_OE_Cil"));
				   	entity.setDe_Para_Perto_OE_Eixo(rs.getString("Para_Perto_OE_Eixo"));
				   	entity.setDe_Sob_ciclo_OE_Esf(rs.getString("Sob_ciclo_OE_Esf"));
				   	entity.setDe_Sob_ciclo_OE_Cil(rs.getString("Sob_ciclo_OE_Cil"));
				   	entity.setDe_Sob_ciclo_OE_Eixo(rs.getString("Sob_ciclo_OE_Eixo"));
				   	entity.setDe_Sob_ciclo_OD_Esf(rs.getString("Sob_ciclo_OD_Esf"));
				   	entity.setDe_Sob_ciclo_OD_Cil(rs.getString("Sob_ciclo_OD_Cil"));
				   	entity.setDe_Sob_ciclo_OD_Eixo(rs.getString("Sob_ciclo_OD_Eixo"));
				   
			   }           
		   }
		   catch(SQLException ex )
		   {
		   }
	        
		   return entity;
	}
	
	public ArrayList listConsultaByProntuario(String auxIntervalo1, String auxIntervalo2) {
		// TODO Stub de método gerado automaticamente
		
		
		
		 //Declarar a variável de retorno
		  ArrayList lista = null;
	 	
		  //Limpar o parâmetro de entrada
		  auxIntervalo1 = auxIntervalo1.trim();
	 	  auxIntervalo2 = auxIntervalo2.trim();
	 	
		  //Tipo de pesquisa a ser realizada
		  String condicao = null;
	 	
		  //Verificar o tipo de pesquisa a ser realizada
		  if(auxIntervalo1.equals("*"))
		  {
			  //Listar todos os registros cadastrados
			  condicao =  "Prontuário > 0";
		  }
		  else
		  {
			  //Listar apenas os registros cujo nome contenha o texto em questão
			  condicao = " Prontuário > "+auxIntervalo1 +" and Prontuário < "+auxIntervalo2;
			  System.out.println(condicao);
		  }
	 	
		  try
		  {
			  //Realizar a pesquisa
			  ResultSet rs = selectDados("Consulta", condicao);
	 	
			  //Verificar o retorno na pesquisa
			  if(rs != null)
			  {
				  //Criar a variável de retorno
				  lista = new ArrayList();
	 			
				  //Montar a listagem de retorno, com os objetos entity
				  while(rs.next())
				  {	 				
					 
				  	ConsultaEntity entity = new ConsultaEntity();
				   	entity.setIn_Prontuário(rs.getInt(1));
				   	
				   	
				   	
				   	entity.setDt_Data(HCalendar.setData(rs.getString("Data")));
				   	entity.setDe_Pupilas(rs.getString("Pupilas"));
				   	entity.setDe_Pálpebras(rs.getString("Pálpebras"));
				   	entity.setDe_Conjuntivas(rs.getString("Conjuntivas"));
				   	entity.setDe_Aparelho_Lacrimal(rs.getString("Aparelho_Lacrimal"));
				   	entity.setDe_Cover_Test(rs.getString("Cover_Test"));
				   	entity.setDe_Hirschberg(rs.getString("Hirschberg"));
				   	entity.setDe_PPC(rs.getString("PPC"));
				   	entity.setIn_RotOdUm(rs.getInt("RotOd1"));
				   	entity.setIn_RotOdDois(rs.getInt("RotOd2"));
				   	entity.setIn_RotOdTres(rs.getInt("RotOd3"));
				   	entity.setIn_RotOdQuatro(rs.getInt("RotOd4"));
				   	entity.setIn_RotOdCinco(rs.getInt("RotOd5"));
				   	entity.setIn_RotOdSeis(rs.getInt("RotOd6"));
				   	entity.setIn_RotOeUm(rs.getInt("RotOe1"));
				   	entity.setIn_RotOeDois(rs.getInt("RotOe2"));
				   	entity.setIn_RotOeTres(rs.getInt("RotOe3"));
				   	entity.setIn_RotOeQuatro(rs.getInt("RotOe4"));
				   	entity.setIn_RotOeCinco(rs.getInt("RotOe5"));
				   	entity.setIn_RotOeSeis(rs.getInt("RotOe6"));
				   	entity.setDe_TBD_OD(rs.getString("TBD_OD"));
				   	entity.setDe_TBD_OE(rs.getString("TBD_OE"));
				   	entity.setDe_TBDHora(rs.getString("TBDHora"));
				   	entity.setIn_Tonometria_OE(rs.getInt("Tonometria_OE"));
				   	entity.setIn_Tonometria_OD(rs.getInt("Tonometria_OD"));
				   	entity.setDe_TonoHora(rs.getString("TonoHora"));
				   	entity.setDt_AutoData(HCalendar.setData(rs.getString("AutoData")));
				   	entity.setDe_AutoHora(rs.getString("AutoHora"));
				   	entity.setDe_AutoDistVertice(rs.getString("AutoDistVertice"));
				   	entity.setDe_DistInterPupilar(rs.getString("DistInterPupilar"));
				   	entity.setDe_Sinal(rs.getString("Sinal"));
				   	entity.setDe_ODCilindroUm(rs.getString("ODCilindro1"));
				   	entity.setDe_ODCilindroDois(rs.getString("ODCilindro2"));
				   	entity.setDe_ODCilindroTres(rs.getString("ODCilindro3"));
				   	entity.setDe_ODEixoUm(rs.getString("ODEixo1"));
				   	entity.setDe_ODEixoDois(rs.getString("ODEixo2"));
				   	entity.setDe_ODEixoTres(rs.getString("ODEixo3"));
				   	entity.setDe_ODErroEsferico(rs.getString("ODErroEsferico"));
				   	entity.setDe_ODEsfericoUm(rs.getString("ODEsferico1"));
				   	entity.setDe_ODEsfericoDois(rs.getString("ODEsferico2"));
				   	entity.setDe_ODEsfericoTres(rs.getString("ODEsferico3"));
				   	entity.setDe_OECilindroUm(rs.getString("OECilindro1"));
				   	entity.setDe_OECilindroDois(rs.getString("OECilindro2"));
				   	entity.setDe_OECilindroTres(rs.getString("OECilindro3"));
				   	entity.setDe_OEEixoUm(rs.getString("OEEixo1"));
				   	entity.setDe_OEEixoDois(rs.getString("OEEixo2"));
				   	entity.setDe_OEEixoTres(rs.getString("OEEixo3"));
				   	entity.setDe_OEErroEsferico(rs.getString("OEErroEsferico"));
				   	entity.setDe_OEEsfericoUm(rs.getString("OEEsferico1"));
				   	entity.setDe_OEEsfericoDois(rs.getString("OEEsferico2"));
				   	entity.setDe_OEEsfericoTres(rs.getString("OEEsferico3"));
				   	entity.setIn_ODCheck(rs.getInt("ODCheck"));
				   	entity.setIn_OECheck(rs.getInt("OECheck"));
				   	entity.setDe_CerOdUm(rs.getString("CerOd1"));
				   	entity.setDe_CerOdDois(rs.getString("CerOd2"));
				   	entity.setDe_CerOeUm(rs.getString("CerOe1"));
				   	entity.setDe_CerOeDois(rs.getString("CerOe2"));
				   	entity.setDe_RetOdUm(rs.getString("RetOd1"));
				   	entity.setDe_RetOdDois(rs.getString("RetOd2"));
				   	entity.setDe_RetOeUm(rs.getString("RetOe1"));
				   	entity.setDe_RetOeDois(rs.getString("RetOe2"));
				   	entity.setDe_Para_Longe_OE(rs.getString("Para_Longe_OE"));
				   	entity.setDe_Para_longe_OD(rs.getString("Para_longe_OD"));
				   	entity.setDe_Para_perto_OE(rs.getString("Para_perto_OE"));
				   	entity.setDe_Para_Perto_OD(rs.getString("Para_Perto_OD"));
				   	entity.setDe_DP(rs.getString("DP"));
				   	entity.setDe_DNP(rs.getString("DNP"));
				   	entity.setDe_Sob_ciclo_OE(rs.getString("Sob_ciclo_OE"));
				   	entity.setDe_Sob_ciclo_OD(rs.getString("Sob_ciclo_OD"));
				   	entity.setDe_Retorno(rs.getBoolean(71));
				   	entity.setDe_Diagnóstico(rs.getString("Diagnóstico"));
				   	entity.setDe_Conduta(rs.getString("Conduta"));
				   	entity.setDe_Anamnese(rs.getString("Anamnese"));
				   	entity.setDe_Biomicroscopia(rs.getString("Biomicroscopia"));
				   	entity.setDe_Oftalmoscopia(rs.getString("Oftalmoscopia"));
				   	entity.setDe_Obs(rs.getString("Obs"));
				   	entity.setDe_DiagExtra(rs.getString("DiagExtra"));
				   	entity.setDe_Convenio(rs.getString("Convenio"));
				   	entity.setDe_Matrícula(rs.getString("Matrícula"));
				   	entity.setIn_Flag(rs.getInt("Flag"));
				   	entity.setDe_DNPOE(rs.getString("DNPOE"));
				   	entity.setDe_AVODSC(rs.getString("AVODSC"));
				   	entity.setDe_AVOESC(rs.getString("AVOESC"));
				   	entity.setDe_AVODCC(rs.getString("AVODCC"));
				   	entity.setDe_AVOECC(rs.getString("AVOECC"));
				   	entity.setDe_Para_Longe_OD_Esf(rs.getString("Para_Longe_OD_Esf"));
				   	entity.setDe_Para_Longe_OD_Cil(rs.getString("Para_Longe_OD_Cil"));
				   	entity.setDe_Para_Longe_OD_Eixo(rs.getString("Para_Longe_OD_Eixo"));
				   	entity.setDe_Para_Longe_OE_Esf(rs.getString("Para_Longe_OE_Esf"));
				   	entity.setDe_Para_Longe_OE_Cil(rs.getString("Para_Longe_OE_Cil"));
				   	entity.setDe_Para_Longe_OE_Eixo(rs.getString("Para_Longe_OE_Eixo"));
				   	entity.setDe_Adicao(rs.getString("Adicao"));
				   	entity.setDe_Para_Perto_OD_Esf(rs.getString("Para_Perto_OD_Esf"));
				   	entity.setDe_Para_Perto_OD_Cil(rs.getString("Para_Perto_OD_Cil"));
				   	entity.setDe_Para_Perto_OD_Eixo(rs.getString("Para_Perto_OD_Eixo"));
				   	entity.setDe_Para_Perto_OE_Esf(rs.getString("Para_Perto_OE_Esf"));
				   	entity.setDe_Para_Perto_OE_Cil(rs.getString("Para_Perto_OE_Cil"));
				   	entity.setDe_Para_Perto_OE_Eixo(rs.getString("Para_Perto_OE_Eixo"));
				   	entity.setDe_Sob_ciclo_OE_Esf(rs.getString("Sob_ciclo_OE_Esf"));
				   	entity.setDe_Sob_ciclo_OE_Cil(rs.getString("Sob_ciclo_OE_Cil"));
				   	entity.setDe_Sob_ciclo_OE_Eixo(rs.getString("Sob_ciclo_OE_Eixo"));
				   	entity.setDe_Sob_ciclo_OD_Esf(rs.getString("Sob_ciclo_OD_Esf"));
				   	entity.setDe_Sob_ciclo_OD_Cil(rs.getString("Sob_ciclo_OD_Cil"));
				   	entity.setDe_Sob_ciclo_OD_Eixo(rs.getString("Sob_ciclo_OD_Eixo"));
					lista.add(entity);
				  }
			  }	 		
		  }
		  catch(SQLException ex)
		  {
		  	System.out.println("erro...: "+ex);
		  }	 	
		  
		  //System.out.println(lista.size());
      
		  return lista;
	  }

	/* (não-Javadoc)
	 * @see componente.consulta.ConsultaDAO#insertConsulta(componente.consulta.ConsultaEntityNovo)
	 */
	public int insertConsulta(ConsultaEntityNovo consulta) {
		// TODO Stub de método gerado automaticamente
		return 0;
	}

	public ArrayList listDataByProntuario(int prontuario) {
		// TODO Stub de método gerado automaticamente
		return null;
	}

	/* (não-Javadoc)
	 * @see componente.consulta.model.ConsultaDAO#findByProntuarioData(java.lang.String, java.lang.String)
	 */
	public ConsultaEntityNovo findByProntuarioData(String prontuario, String data) {
		// TODO Stub de método gerado automaticamente
		return null;
	}

	/* (não-Javadoc)
	 * @see componente.consulta.model.ConsultaDAO#listDataByProntuario(int, int)
	 */
	public ArrayList listDataByProntuario(int prontuario, int fk_medico) {
		// TODO Stub de método gerado automaticamente
		return null;
	}

	/* (não-Javadoc)
	 * @see componente.consulta.model.ConsultaDAO#findByProntuarioData(java.lang.String, java.lang.String, int)
	 */
	public ConsultaEntityNovo findByProntuarioData(String prontuario, String data, int fk_medico) {
		// TODO Stub de método gerado automaticamente
		return null;
	}

	public ArrayList listHistoricoTonometria(int prontuario, int medico) {
		// TODO Auto-generated method stub
		return null;
	}

	public ArrayList listHistoricoRefracao(int prontuario, int medico) {
		// TODO Auto-generated method stub
		return null;
	}



}
