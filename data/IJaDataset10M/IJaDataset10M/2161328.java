package br.gov.sjc.XmlRead;

import android.*;
import org.w3c.dom.Document;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.io.xml.DomDriver;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.provider.Contacts;
import android.util.Log;
import br.gov.sjc.classes.DadosPessoais;
import br.gov.sjc.classes.Documentos;
import br.gov.sjc.classes.Endereco;
import br.gov.sjc.classes.Escolaridade;
import br.gov.sjc.classes.Nome;
import br.gov.sjc.export.EnviarArquivoFTP;
import br.gov.sjc.export.exportarDados;
import br.gov.sjc.socialalimenta.Membro;
import br.gov.sjc.socialalimenta.SocialAlimentaApp;
import br.gov.sjc.socialalimenta.Trabalho;
import br.gov.sjc.socialalimenta.TrabalhoOFR;
import br.gov.sjc.socialalimenta.grupoFamiliar;
import br.gov.sjc.socialalimenta.dao.DadosPessoaisDAO;
import br.gov.sjc.socialalimenta.dao.DocumentosDAO;
import br.gov.sjc.socialalimenta.dao.EnderecoDAO;
import br.gov.sjc.socialalimenta.dao.EscolaridadeDAO;
import br.gov.sjc.socialalimenta.dao.MembroDAO;
import br.gov.sjc.socialalimenta.dao.NomeDAO;
import br.gov.sjc.socialalimenta.dao.TrabalhoDAO;
import br.gov.sjc.socialalimenta.dao.TrabalhoOFRDAO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class MainTest {

    private static File files = new File("/data/data/br.gov.sjc.socialalimenta/files/socialAlimenta.xml");

    private static String file = "/data/data/br.gov.sjc.socialalimenta/files/socialAlimenta.xml";

    private static Context context;

    private static void salvarArquivo(String documento) throws Exception {
        Log.v("MainTest", "salvarArquivo() Abriu");
        String NomeArquivo = "socialAlimenta-";
        NomeArquivo += getMacAddress();
        String tempString = NomeArquivo.replace(':', '-');
        Log.i("WriteFile", "Nome do arquivo:" + tempString);
        try {
            FileOutputStream fos = context.openFileOutput(tempString + ".xml", Context.MODE_APPEND);
            fos.write(documento.getBytes());
            fos.write("\r\n".getBytes());
            fos.flush();
            fos.close();
            Log.i("MainTest", "salvarArquivo() close");
        } catch (Exception E) {
            Log.e("MainTest", "salvarArquivo() ERRO:" + E.getMessage());
        }
    }

    /**
	 * N�O USADO
	 * @param documento
	 * @param file
	 */
    private static void salvarArquivo2(String documento, String file) {
        File path = new File("/data/data/br.gov.sjc.socialalimenta/file/SocialAlimenta.xml");
        try {
            PrintWriter writer = new PrintWriter(path);
            writer.println("<?xml version=\"1.0\" encoding=\"ISO-8859-1\" standalone=\"no\"?>");
            writer.println(documento);
            writer.flush();
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String loadFileAsString(String filePath) throws java.io.IOException {
        StringBuffer fileData = new StringBuffer(1000);
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        char[] buf = new char[1024];
        int numRead = 0;
        while ((numRead = reader.read(buf)) != -1) {
            String readData = String.valueOf(buf, 0, numRead);
            fileData.append(readData);
        }
        reader.close();
        return fileData.toString();
    }

    public static String getMacAddress() {
        try {
            String retorno;
            retorno = loadFileAsString("/sys/class/net/eth0/address").toUpperCase().substring(0, 17);
            return retorno;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String converter(Document document) throws TransformerException {
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        StreamResult result = new StreamResult(new StringWriter());
        DOMSource source = new DOMSource(document);
        transformer.transform(source, result);
        String xmlString = result.getWriter().toString();
        System.out.append(xmlString);
        Log.v("MainTest", "converter Passou");
        return xmlString;
    }

    private static void gerarXmlXStream(Collection<grupoFamiliar> GF) throws Exception {
        Log.e("gerarXmlXStream", "Entrou");
        GF = new ArrayList<grupoFamiliar>();
        XStream xstream = new XStream();
        xstream.alias("SOCIALALIMENTA", SocialAlimentaApp.class);
        xstream.alias("GRUPOFAMILIAR", grupoFamiliar.class);
        xstream.alias("MEMBRO", Membro.class);
        xstream.alias("TRABALHO", Trabalho.class);
        xstream.alias("TRABALHOOFR", TrabalhoOFR.class);
        Log.e("gerarXmlXStream", "xstream.alias");
        SocialAlimentaApp SAapp2 = new SocialAlimentaApp();
        SAapp2.setrData("String Data de inser��o");
        SAapp2.setrMatricula("String Matricula");
        SAapp2.setrNome("String Nome");
        SAapp2.setGrupoFamiliarCollection(GF);
        Log.e("gerarXmlXStream", "xstream.toXML");
        String xml = xstream.toXML(SAapp2);
        salvarArquivo(xml);
    }

    /**
	 * ESCREVE A ESTRUTURA DO XML
	 * @param SAApp
	 * @param nomeArquivo 
	 * @throws Exception
	 */
    private static void gerarXml(SocialAlimentaApp SAApp, String nomeArquivo) throws Exception {
        Log.v("SocialAlimenta", "gerarXml - Iniciou");
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder dbuilder = dbf.newDocumentBuilder();
        Document doc = dbuilder.newDocument();
        Log.v("SocialAlimenta", "gerarXml - doc gerado");
        Log.v("SocialAlimenta", "gerarXml - SOCIALALIMENTA iniciado");
        Element tagSocialAlimenta = doc.createElement("SOCIALALIMENTA");
        Element tagrData = doc.createElement("RDATA");
        Element tagrNome = doc.createElement("RNOME");
        Element tagrMatricula = doc.createElement("RMATRICULA");
        Log.v("SocialAlimenta", "gerarXml - GRUPOSFAMILIARES iniciado");
        Element tagGruposFamiliares = doc.createElement("GRUPOSFAMILIARES");
        tagrData.setTextContent(String.valueOf(SAApp.getrData()));
        tagrNome.setTextContent(String.valueOf(SAApp.getrNome()));
        tagrMatricula.setTextContent(String.valueOf(SAApp.getrMatricula()));
        tagSocialAlimenta.appendChild(tagrData);
        tagSocialAlimenta.appendChild(tagrNome);
        tagSocialAlimenta.appendChild(tagrMatricula);
        Element SiasN;
        Element eEnd;
        Element eNumero;
        Element eAp;
        Element eBloco;
        Element eComplemento;
        Element eReferencia;
        Element eCEP;
        Element eBairro;
        Element lLocalizado;
        Element lMudouse;
        Element lEInexistente;
        Element lSosam;
        Element aaArrozFrequencia;
        Element aaArrozFormaAcesso;
        Element aaLeiteFrequencia;
        Element aaLeiteFormaAcesso;
        Element aaDerivadosFrequencia;
        Element aaDerivadosFormaAcesso;
        Element aaPaesFrequencia;
        Element aaPaesFormaAcesso;
        Element aaManteigaFrequencia;
        Element aaManteigaFormaAcesso;
        Element aaMacarraoFrequencia;
        Element aaMacarraoFormaAcesso;
        Element aaFubaFrequencia;
        Element aaFubaFormaAcesso;
        Element aaFrutasFrequencia;
        Element aaFrutasFormaAcesso;
        Element aaVerdurasFrequencia;
        Element aaVerdurasFormaAcesso;
        Element aaCarneFrequencia;
        Element aaCarneFormaAcesso;
        Element aaOvosFrequencia;
        Element aaOvosFormaAcesso;
        Element aaFarinhasFrequencia;
        Element aaFarinhasFormaAcesso;
        Element isaGestante;
        Element isaGestanteAcompMedico;
        Element isaGestanteAcompNutric;
        Element isaNutriz;
        Element isaNutrizAcompMedico;
        Element isaNutrizAcompNutric;
        Element isaIntolerancia;
        Element isaIntoleranciaLactose;
        Element isaIntoleranciaGluten;
        Element isaIntoleranciaProteina;
        Element isaIntoleranciaOutra;
        Element isaIntoleranciaAcompMedico;
        Element isaIntoleranciaAcompNutric;
        Element isaDiabete;
        Element isaDiabeteAcompMedico;
        Element isaDiabeteAcompNutric;
        Element isaAnemia;
        Element isaAnemiaAcompMedico;
        Element isaAnemiaAcompNutric;
        Element isaHipertensao;
        Element isaHipertensaoAcompMedico;
        Element isaHipertensaoAcompNutric;
        Element isaObesidade;
        Element isaObesidadeAcompMedico;
        Element isaObesidadeAcompNutric;
        Element tagGrupoFamiliar = null;
        Log.v("SocialAlimenta", "gerarXml - tagGrupoFamiliar FOR iniciado");
        for (grupoFamiliar SAA2 : SAApp.getGrupoFamiliarCollection()) {
            tagGrupoFamiliar = doc.createElement("GRUPOFAMILIAR");
            SiasN = doc.createElement("SIASN");
            eEnd = doc.createElement("ENDERECO");
            eNumero = doc.createElement("NUMERO");
            eAp = doc.createElement("AP");
            eBloco = doc.createElement("BLOCO");
            eComplemento = doc.createElement("COMPLEMENTO");
            eReferencia = doc.createElement("REFERENCIA");
            eCEP = doc.createElement("CEP");
            eBairro = doc.createElement("BAIRRO");
            lLocalizado = doc.createElement("LOCALIZADO");
            lMudouse = doc.createElement("MUDOUSE");
            lEInexistente = doc.createElement("ENDINEX");
            lSosam = doc.createElement("SOSAM");
            aaArrozFrequencia = doc.createElement("ARROZFREQUENCIA");
            aaArrozFormaAcesso = doc.createElement("ARROZFORMAACESSO");
            aaLeiteFrequencia = doc.createElement("LEITEFREQUENCIA");
            aaLeiteFormaAcesso = doc.createElement("LEITEFORMAACESSO");
            aaDerivadosFrequencia = doc.createElement("DERIVADOSFREQUENCIA");
            aaDerivadosFormaAcesso = doc.createElement("DERIVADOSFORMAACESSO");
            aaPaesFrequencia = doc.createElement("PAESFREQUENCIA");
            aaPaesFormaAcesso = doc.createElement("PAESFORMAACESSO");
            aaManteigaFrequencia = doc.createElement("MANTEIGAFREQUENCIA");
            aaManteigaFormaAcesso = doc.createElement("MANTEIGAFORMAACESSO");
            aaMacarraoFrequencia = doc.createElement("MACARRAOFREQUENCIA");
            aaMacarraoFormaAcesso = doc.createElement("MACARRAOFORMAACESSO");
            aaFubaFrequencia = doc.createElement("FUBAFREQUENCIA");
            aaFubaFormaAcesso = doc.createElement("FUBAFORMAACESSO");
            aaFrutasFrequencia = doc.createElement("FRUTASFREQUENCIA");
            aaFrutasFormaAcesso = doc.createElement("FRUTASFORMAACESSO");
            aaVerdurasFrequencia = doc.createElement("VERDURASFREQUENCIA");
            aaVerdurasFormaAcesso = doc.createElement("VERDURASFORMAACESSO");
            aaCarneFrequencia = doc.createElement("CARNEFREQUENCIA");
            aaCarneFormaAcesso = doc.createElement("CARNEFORMAACESSO");
            aaOvosFrequencia = doc.createElement("OVOSFREQUENCIA");
            aaOvosFormaAcesso = doc.createElement("OVOSFORMAACESSO");
            aaFarinhasFrequencia = doc.createElement("FARINHASFREQUENCIA");
            aaFarinhasFormaAcesso = doc.createElement("FARINHASFORMAACESSO");
            isaGestante = doc.createElement("GESTANTE");
            isaGestanteAcompMedico = doc.createElement("GESTANTEACOMPMEDICO");
            isaGestanteAcompNutric = doc.createElement("GESTANTEACOMPNUTRIC");
            isaNutriz = doc.createElement("NUTRIZ");
            isaNutrizAcompMedico = doc.createElement("NUTRIZACOMPMEDICO");
            isaNutrizAcompNutric = doc.createElement("NUTRIZACOMPNUTRIC");
            isaIntolerancia = doc.createElement("INTOLERANCIA");
            isaIntoleranciaLactose = doc.createElement("INTOLERANCIALACTOSE");
            isaIntoleranciaGluten = doc.createElement("INTOLERANCIAGLUTEN");
            isaIntoleranciaProteina = doc.createElement("INTOLERANCIAPROTEINA");
            isaIntoleranciaOutra = doc.createElement("INTOLERANCIAOUTRA");
            isaIntoleranciaAcompMedico = doc.createElement("INTOLERANCIAACOMPMEDICO");
            isaIntoleranciaAcompNutric = doc.createElement("INTOLERANCIAACOMPNUTRIC");
            isaDiabete = doc.createElement("DIABETE");
            isaDiabeteAcompMedico = doc.createElement("DIABETEACOMPMEDICO");
            isaDiabeteAcompNutric = doc.createElement("DIABETEACOMPNUTRIC");
            isaAnemia = doc.createElement("ANEMIA");
            isaAnemiaAcompMedico = doc.createElement("ANEMIAACOMPMEDICO");
            isaAnemiaAcompNutric = doc.createElement("ANEMIAACOMPNUTRIC");
            isaHipertensao = doc.createElement("HIPERTENSAO");
            isaHipertensaoAcompMedico = doc.createElement("HIPERTENSAOACOMPMEDICO");
            isaHipertensaoAcompNutric = doc.createElement("HIPERTENSAOACOMPNUTRIC");
            isaObesidade = doc.createElement("OBESIDADE");
            isaObesidadeAcompMedico = doc.createElement("OBESIDADEACOMPMEDICO");
            isaObesidadeAcompNutric = doc.createElement("OBESIDADEACOMPNUTRIC");
            SiasN.setTextContent(String.valueOf(SAA2.getSiasN()));
            eEnd.setTextContent(String.valueOf(SAA2.geteEndereco()));
            eNumero.setTextContent(String.valueOf(SAA2.geteNumero()));
            eAp.setTextContent(SAA2.geteAp());
            eBloco.setTextContent(SAA2.geteBloco());
            eComplemento.setTextContent(SAA2.geteComplemento());
            eReferencia.setTextContent(SAA2.geteReferencia());
            eCEP.setTextContent(SAA2.geteCEP());
            eBairro.setTextContent(SAA2.geteBairro());
            lLocalizado.setTextContent(String.valueOf(SAA2.getlLocalizado()));
            lMudouse.setTextContent(String.valueOf(SAA2.getlMudouse()));
            lEInexistente.setTextContent(SAA2.getlEInexistente());
            lSosam.setTextContent(SAA2.getlSosam());
            aaArrozFrequencia.setTextContent(String.valueOf(SAA2.getAaArrozFrequencia()));
            aaArrozFormaAcesso.setTextContent(String.valueOf(SAA2.getAaArrozFormaAcesso()));
            aaLeiteFrequencia.setTextContent(String.valueOf(SAA2.getAaLeiteFrequencia()));
            aaLeiteFormaAcesso.setTextContent(String.valueOf(SAA2.getAaLeiteFormaAcesso()));
            aaDerivadosFrequencia.setTextContent(String.valueOf(SAA2.getAaDerivadosFrequencia()));
            aaDerivadosFormaAcesso.setTextContent(String.valueOf(SAA2.getAaDerivadosFormaAcesso()));
            aaPaesFrequencia.setTextContent(String.valueOf(SAA2.getAaPaesFrequencia()));
            aaPaesFormaAcesso.setTextContent(String.valueOf(SAA2.getAaPaesFormaAcesso()));
            aaManteigaFrequencia.setTextContent(String.valueOf(SAA2.getAaManteigaFrequencia()));
            aaManteigaFormaAcesso.setTextContent(String.valueOf(SAA2.getAaManteigaFormaAcesso()));
            aaMacarraoFrequencia.setTextContent(String.valueOf(SAA2.getAaMacarraoFrequencia()));
            aaMacarraoFormaAcesso.setTextContent(String.valueOf(SAA2.getAaMacarraoFormaAcesso()));
            aaFubaFrequencia.setTextContent(String.valueOf(SAA2.getAaFubaFrequencia()));
            aaFubaFormaAcesso.setTextContent(String.valueOf(SAA2.getAaFubaFormaAcesso()));
            aaFrutasFrequencia.setTextContent(String.valueOf(SAA2.getAaFrutasFrequencia()));
            aaFrutasFormaAcesso.setTextContent(String.valueOf(SAA2.getAaFrutasFormaAcesso()));
            aaVerdurasFrequencia.setTextContent(String.valueOf(SAA2.getAaVerdurasFrequencia()));
            aaVerdurasFormaAcesso.setTextContent(String.valueOf(SAA2.getAaVerdurasFormaAcesso()));
            aaCarneFrequencia.setTextContent(String.valueOf(SAA2.getAaCarneFrequencia()));
            aaCarneFormaAcesso.setTextContent(String.valueOf(SAA2.getAaCarneFormaAcesso()));
            aaOvosFrequencia.setTextContent(String.valueOf(SAA2.getAaOvosFrequencia()));
            aaOvosFormaAcesso.setTextContent(String.valueOf(SAA2.getAaOvosFormaAcesso()));
            aaFarinhasFrequencia.setTextContent(String.valueOf(SAA2.getAaFarinhasFrequencia()));
            aaFarinhasFormaAcesso.setTextContent(String.valueOf(SAA2.getAaFarinhasFormaAcesso()));
            isaGestante.setTextContent(String.valueOf(SAA2.getIsaGestante()));
            isaGestanteAcompMedico.setTextContent(String.valueOf(SAA2.getIsaGestanteAcompMedico()));
            isaGestanteAcompNutric.setTextContent(String.valueOf(SAA2.getIsaGestanteAcompNutric()));
            isaNutriz.setTextContent(String.valueOf(SAA2.getIsaNutriz()));
            isaNutrizAcompMedico.setTextContent(String.valueOf(SAA2.getIsaNutrizAcompMedico()));
            isaNutrizAcompNutric.setTextContent(String.valueOf(SAA2.getIsaNutrizAcompNutric()));
            isaIntolerancia.setTextContent(String.valueOf(SAA2.getIsaIntolerancia()));
            isaIntoleranciaLactose.setTextContent(String.valueOf(SAA2.getIsaIntoleranciaLactose()));
            isaIntoleranciaGluten.setTextContent(String.valueOf(SAA2.getIsaIntoleranciaGluten()));
            isaIntoleranciaProteina.setTextContent(String.valueOf(SAA2.getIsaIntoleranciaProteina()));
            isaIntoleranciaOutra.setTextContent(String.valueOf(SAA2.getIsaIntoleranciaOutra()));
            isaIntoleranciaAcompMedico.setTextContent(String.valueOf(SAA2.getIsaIntoleranciaAcompMedico()));
            isaIntoleranciaAcompNutric.setTextContent(String.valueOf(SAA2.getIsaIntoleranciaAcompNutric()));
            isaDiabete.setTextContent(String.valueOf(SAA2.getIsaDiabete()));
            isaDiabeteAcompMedico.setTextContent(String.valueOf(SAA2.getIsaDiabeteAcompMedico()));
            isaDiabeteAcompNutric.setTextContent(String.valueOf(SAA2.getIsaDiabeteAcompNutric()));
            isaAnemia.setTextContent(String.valueOf(SAA2.getIsaAnemia()));
            isaAnemiaAcompMedico.setTextContent(String.valueOf(SAA2.getIsaAnemiaAcompMedico()));
            isaAnemiaAcompNutric.setTextContent(String.valueOf(SAA2.getIsaAnemiaAcompNutric()));
            isaHipertensao.setTextContent(String.valueOf(SAA2.getIsaHipertensao()));
            isaHipertensaoAcompMedico.setTextContent(String.valueOf(SAA2.getIsaHipertensaoAcompMedico()));
            isaHipertensaoAcompNutric.setTextContent(String.valueOf(SAA2.getIsaHipertensaoAcompNutric()));
            isaObesidade.setTextContent(String.valueOf(SAA2.getIsaObesidade()));
            isaObesidadeAcompMedico.setTextContent(String.valueOf(SAA2.getIsaObesidadeAcompMedico()));
            isaObesidadeAcompNutric.setTextContent(String.valueOf(SAA2.getIsaObesidadeAcompNutric()));
            tagGrupoFamiliar.appendChild(SiasN);
            tagGrupoFamiliar.appendChild(eEnd);
            tagGrupoFamiliar.appendChild(eNumero);
            tagGrupoFamiliar.appendChild(eAp);
            tagGrupoFamiliar.appendChild(eBloco);
            tagGrupoFamiliar.appendChild(eComplemento);
            tagGrupoFamiliar.appendChild(eReferencia);
            tagGrupoFamiliar.appendChild(eCEP);
            tagGrupoFamiliar.appendChild(eBairro);
            tagGrupoFamiliar.appendChild(lLocalizado);
            tagGrupoFamiliar.appendChild(lMudouse);
            tagGrupoFamiliar.appendChild(lEInexistente);
            tagGrupoFamiliar.appendChild(lSosam);
            tagGrupoFamiliar.appendChild(aaArrozFrequencia);
            tagGrupoFamiliar.appendChild(aaArrozFormaAcesso);
            tagGrupoFamiliar.appendChild(aaLeiteFrequencia);
            tagGrupoFamiliar.appendChild(aaLeiteFormaAcesso);
            tagGrupoFamiliar.appendChild(aaDerivadosFrequencia);
            tagGrupoFamiliar.appendChild(aaDerivadosFormaAcesso);
            tagGrupoFamiliar.appendChild(aaPaesFrequencia);
            tagGrupoFamiliar.appendChild(aaPaesFormaAcesso);
            tagGrupoFamiliar.appendChild(aaManteigaFrequencia);
            tagGrupoFamiliar.appendChild(aaManteigaFormaAcesso);
            tagGrupoFamiliar.appendChild(aaMacarraoFrequencia);
            tagGrupoFamiliar.appendChild(aaMacarraoFormaAcesso);
            tagGrupoFamiliar.appendChild(aaFubaFrequencia);
            tagGrupoFamiliar.appendChild(aaFubaFormaAcesso);
            tagGrupoFamiliar.appendChild(aaFrutasFrequencia);
            tagGrupoFamiliar.appendChild(aaFrutasFormaAcesso);
            tagGrupoFamiliar.appendChild(aaVerdurasFrequencia);
            tagGrupoFamiliar.appendChild(aaVerdurasFormaAcesso);
            tagGrupoFamiliar.appendChild(aaCarneFrequencia);
            tagGrupoFamiliar.appendChild(aaCarneFormaAcesso);
            tagGrupoFamiliar.appendChild(aaOvosFrequencia);
            tagGrupoFamiliar.appendChild(aaOvosFormaAcesso);
            tagGrupoFamiliar.appendChild(aaFarinhasFrequencia);
            tagGrupoFamiliar.appendChild(aaFarinhasFormaAcesso);
            tagGrupoFamiliar.appendChild(isaGestante);
            tagGrupoFamiliar.appendChild(isaGestanteAcompMedico);
            tagGrupoFamiliar.appendChild(isaGestanteAcompNutric);
            tagGrupoFamiliar.appendChild(isaNutriz);
            tagGrupoFamiliar.appendChild(isaNutrizAcompMedico);
            tagGrupoFamiliar.appendChild(isaNutrizAcompNutric);
            tagGrupoFamiliar.appendChild(isaIntolerancia);
            tagGrupoFamiliar.appendChild(isaIntoleranciaLactose);
            tagGrupoFamiliar.appendChild(isaIntoleranciaGluten);
            tagGrupoFamiliar.appendChild(isaIntoleranciaProteina);
            tagGrupoFamiliar.appendChild(isaIntoleranciaOutra);
            tagGrupoFamiliar.appendChild(isaIntoleranciaAcompMedico);
            tagGrupoFamiliar.appendChild(isaIntoleranciaAcompNutric);
            tagGrupoFamiliar.appendChild(isaDiabete);
            tagGrupoFamiliar.appendChild(isaDiabeteAcompMedico);
            tagGrupoFamiliar.appendChild(isaDiabeteAcompNutric);
            tagGrupoFamiliar.appendChild(isaAnemia);
            tagGrupoFamiliar.appendChild(isaAnemiaAcompMedico);
            tagGrupoFamiliar.appendChild(isaAnemiaAcompNutric);
            tagGrupoFamiliar.appendChild(isaHipertensao);
            tagGrupoFamiliar.appendChild(isaHipertensaoAcompMedico);
            tagGrupoFamiliar.appendChild(isaHipertensaoAcompNutric);
            tagGrupoFamiliar.appendChild(isaObesidade);
            tagGrupoFamiliar.appendChild(isaObesidadeAcompMedico);
            tagGrupoFamiliar.appendChild(isaObesidadeAcompNutric);
            Log.v("SocialAlimenta", "gerarXml - MEMBROS iniciado");
            Element tagMembros = doc.createElement("MEMBROS");
            Element tagMembro = null;
            Element mSiasN;
            Element idMembro;
            Element nNome;
            Element nDNasc;
            Element nNomeMae;
            Element nNomePai;
            Element nNaturalCidade;
            Element dpSexo;
            Element dpECivil;
            Element dpEstuda;
            Element dpCor;
            Element dpAlfabetiza;
            Element dpFEscola;
            Element dRG;
            Element dRGExp;
            Element dRGUF;
            Element dRGDExp;
            Element dCPF;
            Element dCPFDExp;
            Element dCTPS;
            Element dCTPSSerie;
            Element dCTPSDExp;
            Element dNIS;
            Element esEnsino;
            Element esECurso;
            Element esESerie;
            Element esEConcluido;
            Element esEEsp;
            for (Membro MembroColl : SAA2.getMembroCollection()) {
                tagMembro = doc.createElement("MEMBRO");
                mSiasN = doc.createElement("mSIASN");
                idMembro = doc.createElement("IDMEMBRO");
                nNome = doc.createElement("NOME");
                nDNasc = doc.createElement("DATANASCIMENTO");
                nNomeMae = doc.createElement("NOMEMAE");
                nNomePai = doc.createElement("NOMEPAI");
                nNaturalCidade = doc.createElement("NATURALCIDADE");
                dpSexo = doc.createElement("SEXO");
                dpECivil = doc.createElement("ESTADOCIVIL");
                dpEstuda = doc.createElement("ESTUDA");
                dpCor = doc.createElement("COR");
                dpAlfabetiza = doc.createElement("ALFABETIZA");
                dpFEscola = doc.createElement("ESCOLA");
                dRG = doc.createElement("RG");
                dRGExp = doc.createElement("RGEXP");
                dRGUF = doc.createElement("RGUF");
                dRGDExp = doc.createElement("RGDATAEXP");
                dCPF = doc.createElement("CPF");
                dCPFDExp = doc.createElement("CPFDATAEXP");
                dCTPS = doc.createElement("CTPS");
                dCTPSSerie = doc.createElement("CTPSSERIE");
                dCTPSDExp = doc.createElement("CTPSDATAEXP");
                dNIS = doc.createElement("NIS");
                esEnsino = doc.createElement("ENSINO");
                esECurso = doc.createElement("CURSO");
                esESerie = doc.createElement("SERIE");
                esEConcluido = doc.createElement("CONCLUIDO");
                esEEsp = doc.createElement("ESPECIAL");
                mSiasN.setTextContent(String.valueOf(MembroColl.getSiasN()));
                idMembro.setTextContent(String.valueOf(MembroColl.getIdMembro()));
                nNome.setTextContent(String.valueOf(MembroColl.getnNome()));
                nDNasc.setTextContent(String.valueOf(MembroColl.getnDNasc()));
                nNomeMae.setTextContent(String.valueOf(MembroColl.getnNomeMae()));
                nNomePai.setTextContent(String.valueOf(MembroColl.getnNomePai()));
                nNaturalCidade.setTextContent(String.valueOf(MembroColl.getnNaturalCidade()));
                dpSexo.setTextContent(String.valueOf(MembroColl.getDpFEscola()));
                dpECivil.setTextContent(String.valueOf(MembroColl.getDpECivil()));
                dpEstuda.setTextContent(String.valueOf(MembroColl.getDpEstuda()));
                dpCor.setTextContent(String.valueOf(MembroColl.getDpCor()));
                dpAlfabetiza.setTextContent(String.valueOf(MembroColl.getDpAlfabetiza()));
                dpFEscola.setTextContent(String.valueOf(MembroColl.getDpFEscola()));
                dRG.setTextContent(String.valueOf(MembroColl.getdRG()));
                dRGExp.setTextContent(String.valueOf(MembroColl.getdRGExp()));
                dRGUF.setTextContent(String.valueOf(MembroColl.getdRGUF()));
                dRGDExp.setTextContent(String.valueOf(MembroColl.getdRGDExp()));
                dCPF.setTextContent(String.valueOf(MembroColl.getdCPF()));
                dCPFDExp.setTextContent(String.valueOf(MembroColl.getdCPFDExp()));
                dCTPS.setTextContent(String.valueOf(MembroColl.getdCTPS()));
                dCTPSSerie.setTextContent(String.valueOf(MembroColl.getdCTPSSerie()));
                dCTPSDExp.setTextContent(String.valueOf(MembroColl.getdCTPSDExp()));
                dNIS.setTextContent(String.valueOf(MembroColl.getdNIS()));
                esEnsino.setTextContent(String.valueOf(MembroColl.getEsEnsino()));
                esECurso.setTextContent(String.valueOf(MembroColl.getEsECurso()));
                esESerie.setTextContent(String.valueOf(MembroColl.getEsESerie()));
                esEConcluido.setTextContent(String.valueOf(MembroColl.getEsEConcluido()));
                esEEsp.setTextContent(String.valueOf(MembroColl.getEsEEsp()));
                tagMembro.appendChild(mSiasN);
                tagMembro.appendChild(idMembro);
                tagMembro.appendChild(nNome);
                tagMembro.appendChild(nDNasc);
                tagMembro.appendChild(nNomeMae);
                tagMembro.appendChild(nNomePai);
                tagMembro.appendChild(nNaturalCidade);
                tagMembro.appendChild(dpSexo);
                tagMembro.appendChild(dpECivil);
                tagMembro.appendChild(dpEstuda);
                tagMembro.appendChild(dpCor);
                tagMembro.appendChild(dpAlfabetiza);
                tagMembro.appendChild(dpFEscola);
                tagMembro.appendChild(dRG);
                tagMembro.appendChild(dRGExp);
                tagMembro.appendChild(dRGUF);
                tagMembro.appendChild(dRGDExp);
                tagMembro.appendChild(dCPF);
                tagMembro.appendChild(dCPFDExp);
                tagMembro.appendChild(dCTPS);
                tagMembro.appendChild(dCTPSSerie);
                tagMembro.appendChild(dCTPSDExp);
                tagMembro.appendChild(dNIS);
                tagMembro.appendChild(esEnsino);
                tagMembro.appendChild(esECurso);
                tagMembro.appendChild(esESerie);
                tagMembro.appendChild(esEConcluido);
                tagMembro.appendChild(esEEsp);
                Log.v("SocialAlimenta", "gerarXml - TRABALHOS iniciado");
                Element tagTrabalhos = doc.createElement("TRABALHOS");
                Element tagTrabalho = null;
                Element Trabalha;
                Element Tipo;
                Element rendimento;
                Element horasMes;
                Element Ocupacao;
                Element INSS;
                Element tSiasN;
                Element tidMembro;
                for (Trabalho TrabalhoColl : MembroColl.getTrabalhoCollection()) {
                    tagTrabalho = doc.createElement("TRABALHO");
                    Trabalha = doc.createElement("TRABALHA");
                    Tipo = doc.createElement("TIPO");
                    rendimento = doc.createElement("RENDIMENTO");
                    horasMes = doc.createElement("HORASMES");
                    Ocupacao = doc.createElement("OCUPACAO");
                    INSS = doc.createElement("INSS");
                    tSiasN = doc.createElement("tSIASN");
                    tidMembro = doc.createElement("tIDMEMBRO");
                    Trabalha.setTextContent(String.valueOf(TrabalhoColl.getTrabalha()));
                    Tipo.setTextContent(String.valueOf(TrabalhoColl.getTipo()));
                    rendimento.setTextContent(String.valueOf(TrabalhoColl.getRendimento()));
                    horasMes.setTextContent(String.valueOf(TrabalhoColl.getHorasMes()));
                    Ocupacao.setTextContent(String.valueOf(TrabalhoColl.getOcupacao()));
                    INSS.setTextContent(String.valueOf(TrabalhoColl.getINSS()));
                    tSiasN.setTextContent(String.valueOf(TrabalhoColl.gettSiasN()));
                    tidMembro.setTextContent(String.valueOf(TrabalhoColl.getTidMembro()));
                    tagTrabalho.appendChild(Trabalha);
                    tagTrabalho.appendChild(Tipo);
                    tagTrabalho.appendChild(rendimento);
                    tagTrabalho.appendChild(horasMes);
                    tagTrabalho.appendChild(Ocupacao);
                    tagTrabalho.appendChild(INSS);
                    tagTrabalho.appendChild(tSiasN);
                    tagTrabalho.appendChild(tidMembro);
                    Log.v("SocialAlimenta", "gerarXml - tagTrabalhos.appendChild");
                    tagTrabalhos.appendChild(tagTrabalho);
                }
                Log.v("SocialAlimenta", "gerarXml - TRABALHOSOFR iniciado");
                Element tagTrabalhosOFR = doc.createElement("TRABALHOSOFR");
                Element tagTrabalhoOFR = null;
                Element tofrSiasN;
                Element tofridMembro;
                Element tofrFonte;
                Element tofrValor;
                Element tofrComprovante;
                for (TrabalhoOFR TrabalhoOFRColl : MembroColl.getTrabalhoOFRCollection()) {
                    tagTrabalhoOFR = doc.createElement("TRABALHOOFR");
                    tofrSiasN = doc.createElement("OFRSIASN");
                    tofridMembro = doc.createElement("OFRIDMEMBRO");
                    tofrFonte = doc.createElement("OFRFONTE");
                    tofrValor = doc.createElement("OFRVALOR");
                    tofrComprovante = doc.createElement("OFRCOMPROVANTE");
                    tofrSiasN.setTextContent(String.valueOf(TrabalhoOFRColl.getSiasN()));
                    tofridMembro.setTextContent(String.valueOf(TrabalhoOFRColl.getIdMembro()));
                    tofrFonte.setTextContent(String.valueOf(TrabalhoOFRColl.getFonte()));
                    tofrValor.setTextContent(String.valueOf(TrabalhoOFRColl.getValor()));
                    tofrComprovante.setTextContent(String.valueOf(TrabalhoOFRColl.getComprovante()));
                    tagTrabalhoOFR.appendChild(tofrSiasN);
                    tagTrabalhoOFR.appendChild(tofridMembro);
                    tagTrabalhoOFR.appendChild(tofrFonte);
                    tagTrabalhoOFR.appendChild(tofrValor);
                    tagTrabalhoOFR.appendChild(tofrComprovante);
                    Log.v("SocialAlimenta", "gerarXml - tagTrabalhosOFR.appendChild");
                    tagTrabalhosOFR.appendChild(tagTrabalhoOFR);
                }
                Log.v("SocialAlimenta", "gerarXml - tagMembro.appendChild");
                tagMembro.appendChild(tagTrabalhosOFR);
                Log.v("SocialAlimenta", "gerarXml - tagMembro.appendChild");
                tagMembro.appendChild(tagTrabalhos);
                Log.v("SocialAlimenta", "gerarXml - tagMembros.appendChild");
                tagMembros.appendChild(tagMembro);
            }
            Log.v("SocialAlimenta", "gerarXml - tagGrupoFamiliar.appendChild");
            tagGrupoFamiliar.appendChild(tagMembros);
            Log.v("SocialAlimenta", "gerarXml - tagGruposFamiliares.appendChild");
            tagGruposFamiliares.appendChild(tagGrupoFamiliar);
        }
        Log.v("SocialAlimenta", "gerarXml - tagSocialAlimenta.appendChild");
        tagSocialAlimenta.appendChild(tagGruposFamiliares);
        Log.v("SocialAlimenta", "gerarXml - doc.appendChild");
        doc.appendChild(tagSocialAlimenta);
        String arquivo = converter(doc);
        Log.v("MainTest", "gerarXml Passou");
        salvarArquivo(arquivo);
    }

    public MainTest(Context context) {
        this.context = context;
    }

    /**
	 * FAZ A LEITURA DOS DADOS PARA PREENCHER A ESTRUTURA DO XML
	 * @param nomeArquivo
	 * @throws Exception
	 */
    public void main(String nomeArquivo) throws Exception {
        Log.v("SocialAlimenta", "MainTest - main()");
        Cursor cVerificaSA = null;
        Cursor cVerificaGF = null;
        Cursor cVerificaM = null;
        Cursor cVerificaT = null;
        Cursor cVerificaOFR = null;
        Log.v("SocialAlimenta", "MainTest - main()- Declarou os Cursores");
        SQLiteDatabase BancoDados = exportarDados.BancoDados;
        Log.v("SocialAlimenta", "MainTest - main()- Abriu o BD");
        if (BancoDados.isOpen()) {
            Log.v("SocialAlimenta", "MainTest - main()- BancoDados.isOpen()=TRUE");
            long contaCursorSA = 0;
            long contaCursorGF = 0;
            long contaCursorM = 0;
            long contaCursorT = 0;
            long contaCursorOFR = 0;
            Log.v("SocialAlimenta", "MainTest - main()- Declarou os Contadores");
            SocialAlimentaApp SAapp = new SocialAlimentaApp();
            Log.v("SocialAlimenta", "MainTest - main()- Criado objeto SAapp");
            SAapp.setrData("10/02/2012");
            SAapp.setrMatricula("000000");
            SAapp.setrNome("User Test");
            Log.v("SocialAlimenta", "MainTest - main()- Sets SAapp");
            Collection<grupoFamiliar> gfCollection = new ArrayList<grupoFamiliar>();
            Log.v("SocialAlimenta", "MainTest - main()- Declarou a gfCollection");
            try {
                String SQLQuery = "Select * from tab_SocialA_Localizado";
                Log.v("SocialAlimenta", "MainTest - main() - Atribuiu SQLQuery");
                cVerificaSA = BancoDados.rawQuery(SQLQuery, null);
                Log.v("SocialAlimenta", "MainTest - main() - Atribuiu cursor cVerificaSA");
                contaCursorSA = cVerificaSA.getCount();
                Log.v("SocialAlimenta", "MainTest - main() - contaCursorSA:" + contaCursorSA);
                cVerificaSA.moveToFirst();
                Log.v("SocialAlimenta", "MainTest - main() - cVerificaSA.moveToFirst()");
            } catch (Exception e) {
                Log.e("SocialAlimenta", "MainTest - main() - SA " + e.getMessage());
            }
            if (contaCursorSA != 0) {
                do {
                    Collection<Membro> MCollection = new ArrayList<Membro>();
                    Log.v("SocialAlimenta", "MainTest - main()- Declarou a MCollection");
                    try {
                        String SQLQuery = "Select * " + "From tab_SocialA_Endereco E, tab_SocialA_Localizado L, tab_SocialA_AcessoAlimen AA, tab_SocialA_IndSegAlimen ISA " + "WHERE " + "E.SiasN = " + cVerificaSA.getInt(cVerificaSA.getColumnIndex("SiasN")) + " AND " + "E.SiasN=AA.SiasN AND " + "E.SiasN=ISA.SiasN AND " + "E.SiasN=E.SiasN " + " ";
                        Log.v("SocialAlimenta", "MainTest - main() - Atribuiu SQLQuery:" + SQLQuery);
                        cVerificaGF = BancoDados.rawQuery(SQLQuery, null);
                        Log.v("SocialAlimenta", "MainTest - main() - Atribuiu cursor cVerificaGF");
                        contaCursorGF = cVerificaGF.getCount();
                        Log.v("SocialAlimenta", "MainTest - main() - contaCursorGF:" + contaCursorGF);
                        cVerificaGF.moveToFirst();
                        Log.v("SocialAlimenta", "MainTest - main() - cVerificaGF.moveToFirst()");
                    } catch (Exception e) {
                        Log.e("SocialAlimenta", "MainTest - main() - GF " + e.getMessage());
                    }
                    if (contaCursorGF != 0) {
                        Log.v("SocialAlimenta", "MainTest - main() contaCursorGF !=0 =" + contaCursorGF);
                        do {
                            try {
                                String SQLQuery = "Select * " + "FROM tab_SocialA_GrupoFamiliar GF, tab_SocialA_DadosPessoais DP, tab_SocialA_Documentos D, tab_SocialA_Escolaridade E, tab_SocialA_Nome N " + "WHERE " + "GF.SiasN = " + cVerificaGF.getInt(cVerificaGF.getColumnIndex("SiasN")) + " AND " + "GF.SiasN = D.SiasN AND GF.[SiasN] = DP.[SiasN] AND  GF.SiasN = E.SiasN AND GF.SiasN = N.SiasN AND GF.idMembro = DP.idMembro AND GF.idMembro = D.idMembro AND GF.idMembro = E.idMembro AND GF.idMembro = N.idMembro " + " ";
                                Log.v("SocialAlimenta", "MainTest - main() - Atribuiu SQLQuery:" + SQLQuery);
                                cVerificaM = BancoDados.rawQuery(SQLQuery, null);
                                Log.v("SocialAlimenta", "MainTest - main() - Atribuiu cursor cVerificaM");
                                contaCursorM = cVerificaM.getCount();
                                Log.v("SocialAlimenta", "MainTest - main() - contaCursorM:" + contaCursorM);
                                cVerificaM.moveToFirst();
                                Log.v("SocialAlimenta", "MainTest - main() - cVerificaM.moveToFirst()");
                            } catch (Exception e) {
                                Log.e("SocialAlimenta", "MainTest - main() M -" + e.getMessage());
                            }
                            if (contaCursorM != 0) {
                                do {
                                    Collection<Trabalho> TCollection = new ArrayList<Trabalho>();
                                    Log.v("SocialAlimenta", "MainTest - main()- Declarou a TCollection");
                                    try {
                                        String SQLQuery = "Select * " + " From tab_SocialA_Trabalho " + " WHERE " + " SiasN = " + cVerificaGF.getInt(cVerificaGF.getColumnIndex("SiasN")) + " AND " + " idMembro = " + cVerificaM.getInt(cVerificaM.getColumnIndex("idMembro")) + " ";
                                        Log.v("SocialAlimenta", "MainTest - main() - Atribuiu SQLQuery:" + SQLQuery);
                                        cVerificaT = BancoDados.rawQuery(SQLQuery, null);
                                        Log.v("SocialAlimenta", "MainTest - main() - Atribuiu cursor cVerificaT");
                                        contaCursorT = cVerificaT.getCount();
                                        Log.v("SocialAlimenta", "MainTest - main() - contaCursorT:" + contaCursorT);
                                        cVerificaT.moveToFirst();
                                        Log.v("SocialAlimenta", "MainTest - main() - cVerificaT.moveToFirst()");
                                    } catch (Exception e) {
                                        Log.e("SocialAlimenta", "MainTest - main() T-" + e.getMessage());
                                    }
                                    if (contaCursorT != 0) {
                                        do {
                                            Trabalho T = null;
                                            T = new Trabalho();
                                            Log.v("SocialAlimenta", "Criado objeto T ");
                                            T.setHorasMes(cVerificaT.getString(cVerificaT.getColumnIndex("HorasporMes")));
                                            T.setINSS(cVerificaT.getString(cVerificaT.getColumnIndex("INSS")));
                                            T.setOcupacao(cVerificaT.getString(cVerificaT.getColumnIndex("Ocupacao")));
                                            T.setRendimento(cVerificaT.getString(cVerificaT.getColumnIndex("Rendimento")));
                                            T.setTipo(cVerificaT.getString(cVerificaT.getColumnIndex("Tipo")));
                                            T.setTrabalha(cVerificaT.getString(cVerificaT.getColumnIndex("Trabalha")));
                                            T.settSiasN(cVerificaT.getString(cVerificaT.getColumnIndex("SiasN")));
                                            T.setTidMembro(cVerificaT.getString(cVerificaT.getColumnIndex("idMembro")));
                                            TCollection.add(T);
                                            Log.v("SocialAlimenta", "MainTest - Inseriu T em TCollection");
                                        } while (cVerificaT.moveToNext());
                                    } else {
                                        Log.v("SocialAlimenta", "MainTest - main - contaCursorT=" + contaCursorT);
                                    }
                                    Collection<TrabalhoOFR> TOFRCollection = new ArrayList<TrabalhoOFR>();
                                    Log.v("SocialAlimenta", "MainTest - main()- Declarou a TOFRCollection");
                                    try {
                                        String SQLQuery = "Select * " + " From tab_SocialA_TrabalhoOFR " + " WHERE " + " SiasN = " + cVerificaGF.getInt(cVerificaGF.getColumnIndex("SiasN")) + " AND " + " idMembro = " + cVerificaM.getInt(cVerificaM.getColumnIndex("idMembro")) + " ";
                                        Log.v("SocialAlimenta", "MainTest - main() - Atribuiu SQLQuery:" + SQLQuery);
                                        cVerificaOFR = BancoDados.rawQuery(SQLQuery, null);
                                        Log.v("SocialAlimenta", "MainTest - main() - Atribuiu cursor cVerificaOFR");
                                        contaCursorOFR = cVerificaOFR.getCount();
                                        Log.v("SocialAlimenta", "MainTest - main() - contaCursorOFR:" + contaCursorOFR);
                                        cVerificaOFR.moveToFirst();
                                        Log.v("SocialAlimenta", "MainTest - main() - cVerificaT.moveToFirst()");
                                    } catch (Exception e) {
                                        Log.e("SocialAlimenta", "MainTest - main() OFR-" + e.getMessage());
                                    }
                                    if (contaCursorOFR != 0) {
                                        do {
                                            TrabalhoOFR TORF = new TrabalhoOFR();
                                            Log.v("SocialAlimenta", "Criado objeto TORF");
                                            TORF.setComprovante(cVerificaOFR.getString(cVerificaOFR.getColumnIndex("Comprovante")));
                                            TORF.setFonte(cVerificaOFR.getString(cVerificaOFR.getColumnIndex("Fonte")));
                                            TORF.setIdMembro(cVerificaOFR.getString(cVerificaOFR.getColumnIndex("idMembro")));
                                            TORF.setSiasN(cVerificaOFR.getString(cVerificaOFR.getColumnIndex("SiasN")));
                                            TORF.setValor(cVerificaOFR.getString(cVerificaOFR.getColumnIndex("Valor")));
                                            TOFRCollection.add(TORF);
                                            Log.v("SocialAlimenta", "MainTest - Inseriu TORF em TOFRCollection");
                                        } while (cVerificaOFR.moveToNext());
                                    } else {
                                        Log.e("SocialAlimenta", "MainTest - main() - contaCursorOFR=" + contaCursorOFR);
                                    }
                                    Membro M = new Membro();
                                    Log.v("SocialAlimenta", "Criado objeto Membro");
                                    M.setIdMembro(cVerificaM.getInt(cVerificaM.getColumnIndex("idMembro")));
                                    M.setSiasN(cVerificaM.getInt(cVerificaM.getColumnIndex("SiasN")));
                                    M.setnNome(cVerificaM.getString(cVerificaM.getColumnIndex("Nome")));
                                    M.setnDNasc(cVerificaM.getString(cVerificaM.getColumnIndex("DNasc")));
                                    M.setnNaturalCidade(cVerificaM.getString(cVerificaM.getColumnIndex("NaturalCidade")));
                                    M.setnNomeMae(cVerificaM.getString(cVerificaM.getColumnIndex("NomeMae")));
                                    M.setnNomePai(cVerificaM.getString(cVerificaM.getColumnIndex("NomePai")));
                                    M.setDpSexo(cVerificaM.getString(cVerificaM.getColumnIndex("Sexo")));
                                    M.setDpECivil(cVerificaM.getString(cVerificaM.getColumnIndex("EstCivil")));
                                    M.setDpEstuda(cVerificaM.getString(cVerificaM.getColumnIndex("Estuda")));
                                    M.setDpCor(cVerificaM.getString(cVerificaM.getColumnIndex("Cor")));
                                    M.setDpAlfabetiza(cVerificaM.getString(cVerificaM.getColumnIndex("Alfabetiza")));
                                    M.setDpFEscola(cVerificaM.getString(cVerificaM.getColumnIndex("FreqEscola")));
                                    M.setdRG(cVerificaM.getString(cVerificaM.getColumnIndex("RG")));
                                    M.setdRGExp(cVerificaM.getString(cVerificaM.getColumnIndex("RGExp")));
                                    M.setdRGDExp(cVerificaM.getString(cVerificaM.getColumnIndex("RGDataExp")));
                                    M.setdRGUF(cVerificaM.getString(cVerificaM.getColumnIndex("RGUF")));
                                    M.setdCPF(cVerificaM.getString(cVerificaM.getColumnIndex("CPF")));
                                    M.setdCPFDExp(cVerificaM.getString(cVerificaM.getColumnIndex("CPFDataExp")));
                                    M.setdCTPS(cVerificaM.getString(cVerificaM.getColumnIndex("CTPS")));
                                    M.setdCTPSSerie(cVerificaM.getString(cVerificaM.getColumnIndex("CTPSSerie")));
                                    M.setdCTPSDExp(cVerificaM.getString(cVerificaM.getColumnIndex("CTPSDataExp")));
                                    M.setdNIS(cVerificaM.getString(cVerificaM.getColumnIndex("NIS")));
                                    M.setEsEnsino(cVerificaM.getString(cVerificaM.getColumnIndex("Ensino")));
                                    M.setEsECurso(cVerificaM.getString(cVerificaM.getColumnIndex("EnsCurso")));
                                    M.setEsESerie(cVerificaM.getString(cVerificaM.getColumnIndex("EnsSerie")));
                                    M.setEsEConcluido(cVerificaM.getString(cVerificaM.getColumnIndex("EnsConcluido")));
                                    M.setEsEEsp(cVerificaM.getString(cVerificaM.getColumnIndex("EnsEsp")));
                                    Log.v("SocialAlimenta", "MainTest - main - Sets em Campos de M");
                                    M.setTrabalhoCollection(TCollection);
                                    Log.v("SocialAlimenta", "MainTest - main - Inseriu TCollection em M");
                                    M.setTrabalhoOFRCollection(TOFRCollection);
                                    Log.v("SocialAlimenta", "MainTest - main - Inseriu TOFRCollection em M");
                                    MCollection.add(M);
                                    Log.v("SocialAlimenta", "MainTest - Inseriu M em MCollection");
                                } while (cVerificaM.moveToNext());
                            } else {
                                Log.e("SocialAlimenta", "MainTest - main - contaCursorM=" + contaCursorM);
                            }
                            grupoFamiliar gf = new grupoFamiliar();
                            Log.v("SocialAlimenta", "Criado objeto gf");
                            gf.setSiasN(cVerificaGF.getInt(cVerificaGF.getColumnIndex("SiasN")));
                            gf.seteEndereco(cVerificaGF.getString(cVerificaGF.getColumnIndex("Endereco")));
                            gf.seteNumero(cVerificaGF.getString(cVerificaGF.getColumnIndex("numero")));
                            gf.seteAp(cVerificaGF.getString(cVerificaGF.getColumnIndex("Ap")));
                            gf.seteBloco(cVerificaGF.getString(cVerificaGF.getColumnIndex("Bloco")));
                            gf.seteComplemento(cVerificaGF.getString(cVerificaGF.getColumnIndex("Complemento")));
                            gf.seteReferencia(cVerificaGF.getString(cVerificaGF.getColumnIndex("Referencia")));
                            gf.seteCEP(cVerificaGF.getString(cVerificaGF.getColumnIndex("CEP")));
                            gf.seteBairro(cVerificaGF.getString(cVerificaGF.getColumnIndex("Bairro")));
                            gf.setlLocalizado(cVerificaGF.getString(cVerificaGF.getColumnIndex("Localizado")));
                            gf.setlMudouse(cVerificaGF.getString(cVerificaGF.getColumnIndex("Mudouse")));
                            gf.setlEInexistente(cVerificaGF.getString(cVerificaGF.getColumnIndex("EndInex")));
                            gf.setlSosam(cVerificaGF.getString(cVerificaGF.getColumnIndex("Sosam")));
                            gf.setAaArrozFrequencia(cVerificaGF.getString(cVerificaGF.getColumnIndex("ArrozFrequencia")));
                            gf.setAaArrozFormaAcesso(cVerificaGF.getString(cVerificaGF.getColumnIndex("ArrozFormaAcesso")));
                            gf.setAaLeiteFrequencia(cVerificaGF.getString(cVerificaGF.getColumnIndex("LeiteFrequencia")));
                            gf.setAaLeiteFormaAcesso(cVerificaGF.getString(cVerificaGF.getColumnIndex("LeiteFormaAcesso")));
                            gf.setAaDerivadosFrequencia(cVerificaGF.getString(cVerificaGF.getColumnIndex("DerivadosFrequencia")));
                            gf.setAaDerivadosFormaAcesso(cVerificaGF.getString(cVerificaGF.getColumnIndex("DerivadosFormaAcesso")));
                            gf.setAaPaesFrequencia(cVerificaGF.getString(cVerificaGF.getColumnIndex("PaesFrequencia")));
                            gf.setAaPaesFormaAcesso(cVerificaGF.getString(cVerificaGF.getColumnIndex("PaesFormaAcesso")));
                            gf.setAaManteigaFrequencia(cVerificaGF.getString(cVerificaGF.getColumnIndex("ManteigaFrequencia")));
                            gf.setAaManteigaFormaAcesso(cVerificaGF.getString(cVerificaGF.getColumnIndex("ManteigaFormaAcesso")));
                            gf.setAaMacarraoFrequencia(cVerificaGF.getString(cVerificaGF.getColumnIndex("MacarraoFrequencia")));
                            gf.setAaMacarraoFormaAcesso(cVerificaGF.getString(cVerificaGF.getColumnIndex("MacarraoFormaAcesso")));
                            gf.setAaFubaFrequencia(cVerificaGF.getString(cVerificaGF.getColumnIndex("FubaFrequencia")));
                            gf.setAaFubaFormaAcesso(cVerificaGF.getString(cVerificaGF.getColumnIndex("FubaFormaAcesso")));
                            gf.setAaFrutasFrequencia(cVerificaGF.getString(cVerificaGF.getColumnIndex("FrutasFrequencia")));
                            gf.setAaFrutasFormaAcesso(cVerificaGF.getString(cVerificaGF.getColumnIndex("FrutasFormaAcesso")));
                            gf.setAaVerdurasFrequencia(cVerificaGF.getString(cVerificaGF.getColumnIndex("VerdurasFrequencia")));
                            gf.setAaVerdurasFormaAcesso(cVerificaGF.getString(cVerificaGF.getColumnIndex("VerdurasFormaAcesso")));
                            gf.setAaCarneFrequencia(cVerificaGF.getString(cVerificaGF.getColumnIndex("CarneFrequencia")));
                            gf.setAaCarneFormaAcesso(cVerificaGF.getString(cVerificaGF.getColumnIndex("CarneFormaAcesso")));
                            gf.setAaOvosFrequencia(cVerificaGF.getString(cVerificaGF.getColumnIndex("OvosFrequencia")));
                            gf.setAaOvosFormaAcesso(cVerificaGF.getString(cVerificaGF.getColumnIndex("OvosFormaAcesso")));
                            gf.setAaFarinhasFrequencia(cVerificaGF.getString(cVerificaGF.getColumnIndex("FarinhasFrequencia")));
                            gf.setAaFarinhasFormaAcesso(cVerificaGF.getString(cVerificaGF.getColumnIndex("FarinhasFormaAcesso")));
                            gf.setIsaGestante(cVerificaGF.getString(cVerificaGF.getColumnIndex("Gestante")));
                            gf.setIsaGestanteAcompMedico(cVerificaGF.getString(cVerificaGF.getColumnIndex("GestanteAcompMedico")));
                            gf.setIsaGestanteAcompNutric(cVerificaGF.getString(cVerificaGF.getColumnIndex("GestanteAcompNutric")));
                            gf.setIsaNutriz(cVerificaGF.getString(cVerificaGF.getColumnIndex("Nutriz")));
                            gf.setIsaNutrizAcompMedico(cVerificaGF.getString(cVerificaGF.getColumnIndex("NutrizAcompMedico")));
                            gf.setIsaNutrizAcompNutric(cVerificaGF.getString(cVerificaGF.getColumnIndex("NutrizAcompNutric")));
                            gf.setIsaIntolerancia(cVerificaGF.getString(cVerificaGF.getColumnIndex("Intolerancia")));
                            gf.setIsaIntoleranciaGluten(cVerificaGF.getString(cVerificaGF.getColumnIndex("IntoleranciaGluten")));
                            gf.setIsaIntoleranciaLactose(cVerificaGF.getString(cVerificaGF.getColumnIndex("IntoleranciaLactose")));
                            gf.setIsaIntoleranciaProteina(cVerificaGF.getString(cVerificaGF.getColumnIndex("IntoleranciaProteina")));
                            gf.setIsaIntoleranciaOutra(cVerificaGF.getString(cVerificaGF.getColumnIndex("IntoleranciaOutra")));
                            gf.setIsaIntoleranciaAcompMedico(cVerificaGF.getString(cVerificaGF.getColumnIndex("IntoleranciaAcompMedico")));
                            gf.setIsaIntoleranciaAcompNutric(cVerificaGF.getString(cVerificaGF.getColumnIndex("IntoleranciaAcompNutric")));
                            gf.setIsaDiabete(cVerificaGF.getString(cVerificaGF.getColumnIndex("Diabete")));
                            gf.setIsaDiabeteAcompMedico(cVerificaGF.getString(cVerificaGF.getColumnIndex("DiabeteAcompMedico")));
                            gf.setIsaDiabeteAcompNutric(cVerificaGF.getString(cVerificaGF.getColumnIndex("DiabeteAcompNutric")));
                            gf.setIsaAnemia(cVerificaGF.getString(cVerificaGF.getColumnIndex("Anemia")));
                            gf.setIsaAnemiaAcompMedico(cVerificaGF.getString(cVerificaGF.getColumnIndex("AnemiaAcompMedico")));
                            gf.setIsaAnemiaAcompNutric(cVerificaGF.getString(cVerificaGF.getColumnIndex("AnemiaAcompNutric")));
                            gf.setIsaHipertensao(cVerificaGF.getString(cVerificaGF.getColumnIndex("Hipertensao")));
                            gf.setIsaHipertensaoAcompMedico(cVerificaGF.getString(cVerificaGF.getColumnIndex("HipertensaoAcompMedico")));
                            gf.setIsaHipertensaoAcompNutric(cVerificaGF.getString(cVerificaGF.getColumnIndex("HipertensaoAcompNutric")));
                            gf.setIsaObesidade(cVerificaGF.getString(cVerificaGF.getColumnIndex("Obesidade")));
                            gf.setIsaObesidadeAcompMedico(cVerificaGF.getString(cVerificaGF.getColumnIndex("ObesidadeAcompMedico")));
                            gf.setIsaObesidadeAcompNutric(cVerificaGF.getString(cVerificaGF.getColumnIndex("ObesidadeAcompNutric")));
                            gf.setMembroCollection(MCollection);
                            Log.v("SocialAlimenta", "MainTest - Inseriu MCollection em gf.setMembroCollection");
                            gfCollection.add(gf);
                            Log.v("SocialAlimenta", "MainTest - Inseriu gf em gfCollection");
                        } while (cVerificaGF.moveToNext());
                    } else {
                        Log.e("SocialAlimenta", "MainTest - main - contaCursorGF=" + contaCursorGF);
                    }
                } while (cVerificaSA.moveToNext());
            } else {
                Log.e("SocialAlimenta", "MainTest - main() - contaCursorSA=" + contaCursorSA);
            }
            SAapp.setGrupoFamiliarCollection(gfCollection);
            Log.v("SocialAlimenta", "MainTest - main - Inseriu gfCollection em SAapp");
            try {
                Log.v("SocialAlimenta", "MainTest - main - Try Gerar XML");
                gerarXml(SAapp, nomeArquivo);
                Log.v("SocialAlimenta", "GerarXML - Gerou");
            } catch (ParserConfigurationException e) {
                Log.e("SocialAlimenta", "MainTest - ERRO ParserConfigurationException: " + e.getMessage());
            } catch (TransformerException e) {
                Log.e("SocialAlimenta", "MainTest - ERRO TransformerException: " + e.getMessage());
            } catch (Exception e) {
                Log.e("SocialAlimenta", "MainTest - ERRO Exception: " + e.getMessage());
            }
            try {
                Log.v("SocialAlimenta", "lerXml - Leu");
                SocialAlimentaApp SAappLeitura = lerXml();
                Log.v("SocialAlimenta", "lerXml - Leu");
                System.out.println(SAappLeitura.toString());
            } catch (Exception err) {
                Log.e("MainTest", "Try lerXml() - ERRO Exception: " + err.getMessage());
            }
        } else {
            Log.e("SocialAlimenta", "MainTest - main() - ERRO: BD n�o aberto");
        }
    }

    /**
	 * EM USO
	 * @param elem
	 * @param tagName
	 * @return child.getTextContent or Null
	 * @throws Exception
	 * @tutorial http://mballem.wordpress.com/2011/04/22/manipulando-arquivo-xml-%E2%80%93-parte-i-api-nativa/
	 */
    public static String getChildTagValue(Element elem, String tagName) throws Exception {
        NodeList children = elem.getElementsByTagName(tagName);
        String result = null;
        if (children == null) {
            Log.v("MainTest", "getChildTagValue - children == null");
            return result;
        }
        Element child = (Element) children.item(0);
        if (child == null) {
            Log.v("MainTest", "getChildTagValue - child == null");
            return result;
        }
        result = child.getTextContent();
        return result;
    }

    /**
	 * L� O XML E GRAVA TUDO NAS CLASSES
	 * @return SocialAlimentaApp
	 * @throws Exception e, err
	 * @throws SAXException e
	 * @throws TransformerException e
	 * @tutorial http://mballem.wordpress.com/2011/04/22/manipulando-arquivo-xml-%E2%80%93-parte-i-api-nativa/
	 * @tutorial http://stackoverflow.com/questions/2104879/parsing-xml-truncates-file-path
	 */
    private static SocialAlimentaApp lerXml() throws Exception, SAXException, TransformerException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        String NomeArquivo = "socialAlimenta";
        EnviarArquivoFTP dftp = new EnviarArquivoFTP();
        if (dftp.downloadFTP("10.1.30.1", "ntadm/social.alimenta", "so1234", "tablet_ftp/Novo", "/data/data/br.gov.sjc.socialalimenta/files/", NomeArquivo + ".xml")) {
            Log.i("lerXml", "Nome do arquivo:" + NomeArquivo + ".xml");
        } else {
            Log.e("lerXml", "ERRO: N�o Baixou o Arquivo");
        }
        Document doc = db.parse(new InputSource(new FileReader(file.toString())));
        Element socialAlimenta = doc.getDocumentElement();
        Log.i("lerXml", "Criamos um objeto Element que receber� as informa��es de doc");
        Collection<grupoFamiliar> grupoFamiliarCollection = new ArrayList<grupoFamiliar>();
        NodeList GFsList = socialAlimenta.getElementsByTagName("GRUPOSFAMILIARES");
        Element grupoFamiliar;
        Log.i("lerXml", "Criamos a Cole��o grupoFamiliarCollection");
        for (int i = 0; i < GFsList.getLength(); i++) {
            grupoFamiliar = (Element) GFsList.item(i);
            grupoFamiliar gf = new grupoFamiliar();
            Endereco end = new Endereco();
            Log.i("lerXml", "Criamos os objetos de GF e End");
            gf.setSiasN(Integer.parseInt(getChildTagValue(grupoFamiliar, "SIASN")));
            end.setSiasN(gf.getSiasN());
            Log.i("lerXml", "Criamos SIASN:" + gf.getSiasN());
            gf.seteEndereco(getChildTagValue(grupoFamiliar, "ENDERECO"));
            end.setEndereco(gf.geteEndereco());
            gf.seteNumero(getChildTagValue(grupoFamiliar, "NUMERO"));
            end.setNumero(gf.geteNumero());
            gf.seteAp(getChildTagValue(grupoFamiliar, "AP"));
            end.setAp(gf.geteAp());
            gf.seteBloco(getChildTagValue(grupoFamiliar, "BLOCO"));
            end.setBloco(gf.geteBloco());
            gf.seteComplemento(getChildTagValue(grupoFamiliar, "COMPLEMENTO"));
            end.setComplemento(gf.geteComplemento());
            gf.seteReferencia(getChildTagValue(grupoFamiliar, "REFERENCIA"));
            end.setReferencia(gf.geteReferencia());
            gf.seteCEP(getChildTagValue(grupoFamiliar, "CEP"));
            end.setCEP(gf.geteCEP());
            gf.seteBairro(getChildTagValue(grupoFamiliar, "BAIRRO"));
            end.setBairro(gf.geteBairro());
            EnderecoDAO endDao = new EnderecoDAO(context);
            endDao.inserir(end);
            Collection<Membro> membroCollection = new ArrayList<Membro>();
            NodeList MembrosList = grupoFamiliar.getElementsByTagName("MEMBRO");
            Element Membro;
            for (int j = 0; j < MembrosList.getLength(); j++) {
                Membro = (Element) MembrosList.item(j);
                Membro membro = new Membro();
                MembroDAO membroDao = new MembroDAO(context);
                membro.setSiasN(Integer.parseInt(getChildTagValue(Membro, "mSIASN")));
                membro.setIdMembro(Integer.parseInt(getChildTagValue(Membro, "IDMEMBRO")));
                Nome nomeLer = new Nome();
                nomeLer.setSiasN(membro.getSiasN());
                nomeLer.setIdMembro(membro.getIdMembro());
                membro.setnNome(getChildTagValue(Membro, "NOME"));
                nomeLer.setNome(membro.getnNome());
                membro.setnDNasc(getChildTagValue(Membro, "DNASC"));
                nomeLer.setDNasc(membro.getnDNasc());
                membro.setnNomeMae(getChildTagValue(Membro, "NOMEMAE"));
                nomeLer.setNomeMae(membro.getnNomeMae());
                membro.setnNomePai(getChildTagValue(Membro, "NOMEPAI"));
                nomeLer.setNomePai(membro.getnNomePai());
                membro.setnNaturalCidade(getChildTagValue(Membro, "NATURALCIDADE"));
                nomeLer.setNaturalCidade(membro.getnNaturalCidade());
                NomeDAO ndao = new NomeDAO(context);
                ndao.inserir(nomeLer);
                DadosPessoais dpLer = new DadosPessoais();
                dpLer.setSiasN(membro.getSiasN());
                dpLer.setIdMembro(membro.getIdMembro());
                membro.setDpSexo(getChildTagValue(Membro, "SEXO"));
                dpLer.setSexo(membro.getDpSexo());
                membro.setDpECivil(getChildTagValue(Membro, "ESTADOCIVIL"));
                dpLer.setEstCivil(membro.getDpECivil());
                membro.setDpEstuda(getChildTagValue(Membro, "ESTUDA"));
                dpLer.setEstuda(membro.getDpEstuda());
                membro.setDpCor(getChildTagValue(Membro, "COR"));
                dpLer.setCor(membro.getDpCor());
                membro.setDpAlfabetiza(getChildTagValue(Membro, "ALFABETIZA"));
                dpLer.setAlfabetiza(membro.getDpAlfabetiza());
                membro.setDpFEscola(getChildTagValue(Membro, "ESCOLA"));
                dpLer.setFreqEscola(membro.getDpFEscola());
                Documentos docsLer = new Documentos();
                docsLer.setSiasN(membro.getSiasN());
                docsLer.setIdMembro(membro.getIdMembro());
                membro.setdRG(getChildTagValue(Membro, "RG"));
                membro.setdRGExp(getChildTagValue(Membro, "RGEXP"));
                membro.setdRGDExp(getChildTagValue(Membro, "RGDATAEXP"));
                membro.setdRGUF(getChildTagValue(Membro, "RGUF"));
                membro.setdCPF(getChildTagValue(Membro, "CPF"));
                membro.setdCPFDExp(getChildTagValue(Membro, "CPFDATAEXP"));
                membro.setdCTPS(getChildTagValue(Membro, "CTPS"));
                membro.setdCTPSDExp(getChildTagValue(Membro, "CTPSDATAEXP"));
                membro.setdCTPSSerie(getChildTagValue(Membro, "CTPSSERIE"));
                membro.setdNIS(getChildTagValue(Membro, "NIS"));
                Escolaridade escLer = new Escolaridade();
                escLer.setSiasN(membro.getSiasN());
                escLer.setIdMembro(membro.getIdMembro());
                membro.setEsEnsino(getChildTagValue(Membro, "ENSINO"));
                membro.setEsECurso(getChildTagValue(Membro, "CURSO"));
                membro.setEsESerie(getChildTagValue(Membro, "SERIE"));
                membro.setEsEConcluido(getChildTagValue(Membro, "CONCLUIDO"));
                membro.setEsEEsp(getChildTagValue(Membro, "ESPECIAL"));
                Collection<Trabalho> trabalhoCollection = new ArrayList<Trabalho>();
                NodeList trabalhosList = Membro.getElementsByTagName("TRABALHO");
                Element trabalho;
                for (int k = 0; k < trabalhosList.getLength(); k++) {
                    trabalho = (Element) trabalhosList.item(k);
                    Trabalho tr = new Trabalho();
                    tr.settSiasN(getChildTagValue(trabalho, "tSIASN"));
                    tr.setTidMembro(getChildTagValue(trabalho, "tIDMEMBRO"));
                    tr.setTrabalha(getChildTagValue(trabalho, "TRABALHA"));
                    tr.setTipo(getChildTagValue(trabalho, "TIPO"));
                    tr.setRendimento(getChildTagValue(trabalho, "RENDIMENTO"));
                    tr.setHorasMes(getChildTagValue(trabalho, "HORASMES"));
                    tr.setOcupacao(getChildTagValue(trabalho, "OCUPACAO"));
                    tr.setINSS(getChildTagValue(trabalho, "INSS"));
                    trabalhoCollection.add(tr);
                    TrabalhoDAO trdao = new TrabalhoDAO(context);
                    trdao.inserir(tr);
                }
                membro.setTrabalhoCollection(trabalhoCollection);
                Collection<TrabalhoOFR> trabalhoOFRCollection = new ArrayList<TrabalhoOFR>();
                NodeList trabalhosOFRList = Membro.getElementsByTagName("TRABALHOOFR");
                Element trabalhoOFR;
                for (int l = 0; l < trabalhosOFRList.getLength(); l++) {
                    trabalhoOFR = (Element) trabalhosOFRList.item(l);
                    TrabalhoOFR trOFR = new TrabalhoOFR();
                    trOFR.setSiasN(getChildTagValue(trabalhoOFR, "OFRSIASN"));
                    trOFR.setIdMembro(getChildTagValue(trabalhoOFR, "OFRIDMEMBRO"));
                    trOFR.setFonte(getChildTagValue(trabalhoOFR, "OFRFONTE"));
                    trOFR.setValor(getChildTagValue(trabalhoOFR, "OFRVALOR"));
                    trOFR.setComprovante(getChildTagValue(trabalhoOFR, "OFRCOMPROVANTE"));
                    trabalhoOFRCollection.add(trOFR);
                    TrabalhoOFRDAO trOFRdao = new TrabalhoOFRDAO(context);
                    trOFRdao.inserir(trOFR);
                }
                membro.setTrabalhoOFRCollection(trabalhoOFRCollection);
                membroCollection.add(membro);
            }
            gf.setMembroCollection(membroCollection);
            grupoFamiliarCollection.add(gf);
        }
        SocialAlimentaApp SAapp = new SocialAlimentaApp();
        SAapp.setrData(getChildTagValue(socialAlimenta, "RDATA"));
        SAapp.setrMatricula(getChildTagValue(socialAlimenta, "RMATRICULA"));
        SAapp.setrNome(getChildTagValue(socialAlimenta, "RNOME"));
        SAapp.setGrupoFamiliarCollection(grupoFamiliarCollection);
        return SAapp;
    }

    /**
	 * Baixa Arquivo XML do FTP, salva internamente, le o arquivo.
	 * @param nomeArquivo
	 * @return
	 * @throws SocketException
	 * @throws IOException
	 */
    private static SocialAlimentaApp lerXStrean(String nomeArquivo) throws SocketException, IOException {
        EnviarArquivoFTP eaf = new EnviarArquivoFTP();
        boolean resposta = eaf.downloadFTP("10.1.30.1", "ntadm/social.alimenta", "so1234", "tablet_ftp", "/data/data/br.gov.sjc.socialalimenta/files/", nomeArquivo);
        FileReader reader = null;
        SocialAlimentaApp SAapp = null;
        String XmlText = "";
        if (resposta) {
            FileReader fileReader = null;
            try {
                File root = Environment.getExternalStorageDirectory();
                fileReader = new FileReader(root + "/socialAlimenta/socialAlimenta.xml");
                BufferedReader leitor = new BufferedReader(fileReader);
                String linha = null;
                while ((linha = leitor.readLine()) != null) {
                    XmlText += linha;
                }
                Log.i("SocialAlimenta", "lerXStrean - executado o reader e o XmlText:" + XmlText);
            } catch (FileNotFoundException e) {
                Log.e("SocialAlimenta", "lerXStrean - ERRO:" + e.getMessage() + "---XmlText:" + XmlText);
            }
            XStream xStream = new XStream(new DomDriver());
            SAapp = (SocialAlimentaApp) xStream.fromXML(fileReader);
            Log.e("TAG", "" + SAapp.toString());
            Log.v("SocialAlimenta", "" + SAapp.toString());
        } else {
            Log.e("SocialAlimenta", "N�o realizado o Download:" + SAapp.toString());
        }
        return SAapp;
    }
}
