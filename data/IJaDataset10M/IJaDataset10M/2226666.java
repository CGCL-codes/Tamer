package telas.componentes;

import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.swing.JTable;
import javax.swing.table.TableColumn;
import business.sistema.ExceptionSistema;
import business.sistema.IObjetoSistema;
import business.sistema.Observer;
import business.sistema.Subject;
import business.util.Menssagens;

public class Tabela extends JTable implements Subject, Observer {

    private static final long serialVersionUID = -3912449179342098965L;

    private ArrayList<IObjetoSistema> dados;

    private ArrayList<String[]> colunas = null;

    private ArrayList<Observer> observers = null;

    private String atributoObservado = "";

    private String condicao = "";

    private boolean multiplaEscolha;

    public boolean isMultiplaEscolha() {
        return multiplaEscolha;
    }

    public void setMultiplaEscolha(boolean multiplaEscolha) {
        this.multiplaEscolha = multiplaEscolha;
    }

    /**
	 * Construtor
	 */
    public Tabela() {
        super();
        setFillsViewportHeight(true);
        initialize();
    }

    /**
	 * This method initializes this
	 * 
	 */
    private void initialize() {
        this.addKeyListener(new java.awt.event.KeyAdapter() {

            public void keyReleased(java.awt.event.KeyEvent e) {
                try {
                    notifyObservers();
                } catch (Exception ex) {
                    new ExceptionSistema(ex);
                }
            }
        });
        this.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseClicked(java.awt.event.MouseEvent e) {
                try {
                    notifyObservers();
                } catch (Exception ex) {
                    new ExceptionSistema(ex);
                }
            }
        });
    }

    /**
	 * Ligar os dados à tabela.
	 * 
	 * @throws Exception
	 */
    public void bind() throws Exception {
        this.setModel(new TableModelSistema(getColunas(), formataDados()));
        aplicaTamanhoColuna();
    }

    /**
	 * Retorna o objeto Corrente.
	 * 
	 * @return dados : IObjetoSistema - Objeto corrente.
	 */
    public IObjetoSistema getObjetoCorrente() throws Exception {
        IObjetoSistema objetoSelecionado = null;
        int numeroLinhasSelecionadas = getSelectedRowCount();
        if (numeroLinhasSelecionadas == 1) {
            objetoSelecionado = (IObjetoSistema) dados.get(getSelectedRow());
        } else {
            if (numeroLinhasSelecionadas < 1) {
                throw new Exception(Menssagens.selecaoItem);
            } else if (!isMultiplaEscolha()) {
                throw new Exception(Menssagens.selecaoUnica);
            }
        }
        return objetoSelecionado;
    }

    /**
	 * Formata os dados para ser mostrados na tabela.
	 * 
	 * @return listaDados : ArrayList<Objeto[]> - Lista de dados.
	 */
    private ArrayList<Object[]> formataDados() throws Exception {
        ArrayList<Object[]> lstDados = new ArrayList<Object[]>();
        int numeroColunas = getColunas().size();
        Method objMetodo = null;
        for (Object objetoDados : getDados()) {
            Object[] objetos = new Object[numeroColunas];
            int i = 0;
            for (String[] nomeColuna : colunas) {
                Class<?> objClasse = objetoDados.getClass();
                Object objeto;
                objMetodo = objClasse.getMethod(nomeColuna[1]);
                objeto = objMetodo.invoke(objetoDados);
                if (objeto instanceof Date) {
                    SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                    objeto = formato.format(objeto);
                } else if (objeto instanceof Double) {
                    DecimalFormat decimal = new DecimalFormat(" #,##0.00");
                    objeto = decimal.format(objeto);
                }
                objetos[i] = objeto;
                i++;
            }
            lstDados.add(objetos);
        }
        return lstDados;
    }

    /**
	 * Método responsável por configurar largura das colunas
	 */
    private void aplicaTamanhoColuna() {
        ArrayList<String[]> colunas = getColunas();
        for (int i = 0; i < colunas.size(); i++) {
            String[] coluna = colunas.get(i);
            if (coluna.length == 3) {
                TableColumn tcColuna = getColumnModel().getColumn(i);
                tcColuna.setPreferredWidth(Integer.parseInt(coluna[2]));
            }
        }
    }

    /**
	 * Retorna os dados da tabela.
	 * 
	 * @return dados : ArrayList<IObjetoSistema>
	 */
    public ArrayList<IObjetoSistema> getDados() {
        if (dados == null) {
            dados = new ArrayList<IObjetoSistema>();
        }
        return dados;
    }

    /**
	 * Informa qual a lista de dados da tabela.
	 * 
	 * @param dados : ArrayList<IObjetoSistema>
	 */
    public void setDados(ArrayList<IObjetoSistema> dados) {
        this.dados = dados;
    }

    public void setDados(List<IObjetoSistema> dados) {
        this.dados = new ArrayList<IObjetoSistema>();
        this.dados.addAll(dados);
    }

    public void setDados(Set<IObjetoSistema> dados) {
        this.dados = new ArrayList<IObjetoSistema>();
        this.dados.addAll(dados);
    }

    /**
	 * Retorna as colunas da tabela.
	 * 
	 * @return colunas : ArrayList<String[]>
	 */
    public ArrayList<String[]> getColunas() {
        if (colunas == null) {
            colunas = new ArrayList<String[]>();
        }
        return colunas;
    }

    /**
	 * Informa quais as colunas da tabela.
	 * 
	 * @param colunas : ArrayList<String[]>
	 */
    public void setColunas(ArrayList<String[]> colunas) {
        this.colunas = colunas;
    }

    /**
	 * Notifica os componentes observadores
	 */
    public void notifyObservers() throws Exception {
        for (Observer observer : getObservers()) {
            observer.update(getObjetoCorrente(), "Objeto");
        }
    }

    public void registerObserver(Observer observer) throws Exception {
        getObservers().add(observer);
    }

    public void removeObserver(Observer observer) throws Exception {
        getObservers().remove(observer);
    }

    public ArrayList<Observer> getObservers() {
        if (observers == null) {
            observers = new ArrayList<Observer>();
        }
        return observers;
    }

    @SuppressWarnings("unchecked")
    public void update(IObjetoSistema objeto, String mensagem) throws Exception {
        Class<?> clsObjeto = objeto.getClass();
        ArrayList<IObjetoSistema> lista = new ArrayList<IObjetoSistema>();
        Method objMetodo = clsObjeto.getMethod("get" + getAtributoObservado());
        for (IObjetoSistema objetoLista : (Set<IObjetoSistema>) objMetodo.invoke(objeto)) {
            if (!getCondicao().equals("")) {
                Class<?> classeObjetoLista = objetoLista.getClass();
                Method metodoCondicao = classeObjetoLista.getMethod("get" + getCondicao());
                if (metodoCondicao.invoke(objetoLista) == null) lista.add(objetoLista);
            } else {
                lista.add(objetoLista);
            }
        }
        setDados(lista);
        formataDados();
        bind();
    }

    public String getAtributoObservado() {
        return atributoObservado;
    }

    public void setAtributoObservado(String atributoObservado) {
        this.atributoObservado = atributoObservado;
    }

    public String getCondicao() {
        return condicao;
    }

    public void setCondicao(String condicao) {
        this.condicao = condicao;
    }
}
