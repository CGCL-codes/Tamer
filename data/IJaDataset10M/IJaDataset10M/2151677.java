package br.org.acessobrasil.portal.persistencia.sinc;

import java.io.IOException;
import java.io.StringReader;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.commons.httpclient.HttpException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import br.org.acessobrasil.correiobraille.dao.CbrMensagemDao;
import br.org.acessobrasil.correiobraille.modelo.CbrMensagem;
import br.org.acessobrasil.correiobraille.util.CbrDES;
import br.org.acessobrasil.portal.util.MySqlConnection;
import br.org.acessobrasil.portal.util.PPW;

public class Cliente extends SuperSinc {

    private CbrMensagemDao cbrMensagemDao = new CbrMensagemDao();

    /**
	 * Solicita a atualizacao do banco local ao servidor, sincronizando a tabela solicitada apartir de algum id
	 * @param tabela
	 * @param inicio
	 */
    public void atualizarInformacao(String tabela, Long inicio) {
        try {
            PPW ppw = new PPW();
            String url = servidorUrl + "?dt=" + new Date().getTime() + "&t=" + URLEncoder.encode(CbrDES.encriptar(tabela.toString(), chave), "UTF-8") + "&i=" + URLEncoder.encode(CbrDES.encriptar(inicio.toString(), chave), "UTF-8");
            String codXml = ppw.getContent(url).trim();
            codXml = CbrDES.decriptar(codXml.toString(), chave);
            if (codXml.equals("") || codXml == null || codXml.length() < 5) throw new Exception();
            lerXml(codXml);
        } catch (HttpException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
	 * Le o xml recebido do servidor e sincroniza o banco com as 
	 * informacoes inserindo as cartas no banco local
	 * @param xml
	 */
    public void lerXml(String xml) {
        CbrMensagem cbrMensagem = new CbrMensagem();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder parser = factory.newDocumentBuilder();
            InputSource input = new InputSource(new StringReader(xml));
            Document doc = parser.parse(input);
            NodeList listaElementos = doc.getElementsByTagName("mensagem");
            for (int i = 0; i < listaElementos.getLength(); i++) {
                Element filho = (Element) listaElementos.item(i);
                NodeList listaFilho = filho.getChildNodes();
                for (int j = 0; j < listaFilho.getLength(); j++) {
                    Element child = (Element) listaFilho.item(j);
                    switch(j) {
                        case 0:
                            cbrMensagem.setIdMensagem(Long.parseLong(child.getFirstChild().getNodeValue()));
                            break;
                        case 1:
                            cbrMensagem.setIdUsuario(Long.parseLong(child.getFirstChild().getNodeValue()));
                            break;
                        case 2:
                            long dt = Long.parseLong(child.getFirstChild().getNodeValue());
                            Date date = new Date(dt);
                            cbrMensagem.setDatahora(date);
                            break;
                        case 3:
                            cbrMensagem.setTexto(child.getFirstChild().getNodeValue());
                            break;
                        case 4:
                            cbrMensagem.setRemetente(child.getFirstChild().getNodeValue());
                            break;
                        case 5:
                            cbrMensagem.setDestinatario(child.getFirstChild().getNodeValue());
                            break;
                        case 6:
                            cbrMensagem.setImpresso(Boolean.parseBoolean(child.getFirstChild().getNodeValue()));
                            break;
                        case 7:
                            cbrMensagem.setNomeUsuario(child.getFirstChild().getNodeValue());
                            break;
                        case 8:
                            cbrMensagem.setEmailRemetente(child.getFirstChild().getNodeValue());
                            break;
                    }
                }
                inserirNoBD(cbrMensagem, "cbr_mensagem");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
	 * Pegar o ultimo id para sincronizar o banco cliente com o servidor apartir dele
	 * @return quantidade de linhas de mensagens no banco
	 */
    public int ultimoIdBD(String tabela) throws SQLException, IOException, ClassNotFoundException, Exception {
        Connection con = MySqlConnection.getConnection();
        ResultSet rs = null;
        Statement st = null;
        int ini = 0;
        try {
            st = con.createStatement();
            rs = st.executeQuery("select * from " + tabela + " order by id_mensagem desc");
            if (rs.next()) {
                ini = rs.getInt("id_mensagem");
            }
        } catch (Exception w) {
            w.printStackTrace();
        } finally {
            st.close();
            rs.close();
            con.close();
        }
        return ini;
    }

    /**
	 * Insere a mensagem no banco local(cliente)
	 * @param cbrMensagem
	 * @param tabela
	 * @throws Exception
	 */
    public void inserirNoBD(CbrMensagem cbrMensagem, String tabela) throws Exception {
        Connection con = MySqlConnection.getConnection();
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement("insert into " + tabela + " (id_mensagem, datahora, destinatario, impresso, remetente, texto, id_usuario, nome_usuario, email_remetente) values (?,?,?,?,?,?,?,?,?)");
            ps.setLong(1, cbrMensagem.getIdMensagem());
            java.sql.Timestamp data = new java.sql.Timestamp(cbrMensagem.getDatahora().getTime());
            ps.setTimestamp(2, data);
            ps.setString(3, cbrMensagem.getDestinatario());
            ps.setBoolean(4, cbrMensagem.isImpresso());
            ps.setString(5, cbrMensagem.getRemetente());
            ps.setString(6, cbrMensagem.getTexto());
            ps.setLong(7, cbrMensagem.getIdUsuario());
            ps.setString(8, cbrMensagem.getNomeUsuario());
            ps.setString(9, cbrMensagem.getEmailRemetente());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ps.close();
            con.close();
        }
    }

    public CbrMensagemDao getCbrMensagemDao() {
        return cbrMensagemDao;
    }

    public void setCbrMensagemDao(CbrMensagemDao cbrMensagemDao) {
        this.cbrMensagemDao = cbrMensagemDao;
    }
}
