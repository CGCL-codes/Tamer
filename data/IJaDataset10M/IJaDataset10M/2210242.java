package trabalho.comum;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

public class Menu {

    /**
	 * Para mostrar um titulo
	 * @param titulo
	 */
    public static void Titulo(String titulo) {
        System.out.println(titulo + ":");
    }

    /**
	 * Para mostrar texto de menu
	 * @param texto
	 */
    public static void Texto(String texto) {
        System.out.println(" - " + texto);
    }

    /**
	 * Para mostrar um separador de menu
	 * @param titulo
	 */
    public static void Separador(String titulo) {
        System.out.println("\t" + titulo);
    }

    /**
	 * Para mostrar uma op��o de menu
	 * @param letra
	 * @param descricao
	 */
    public static void Opcao(String letra, String descricao) {
        System.out.println(letra + " - " + descricao);
    }

    /**
	 * Para mostrar uma op��o de menu
	 * @param numero
	 * @param descricao
	 */
    public static void Opcao(int numero, String descricao) {
        System.out.println(numero + " - " + descricao);
    }

    /**
	 * Para mensagens de aviso
	 * @param texto
	 */
    public static void Aviso(String texto) throws IOException {
        BufferedReader d = new BufferedReader(new InputStreamReader(System.in));
        System.out.println(" ! " + texto);
        d.read();
    }

    /**
	 * Para mensagens de erro de menu
	 * @param texto
	 * @throws IOException
	 */
    public static void Erro(String texto) throws IOException {
        BufferedReader d = new BufferedReader(new InputStreamReader(System.in));
        System.err.println(" X " + texto);
        d.read();
    }

    /**
	 * Para leitura de ints
	 * @return
	 * @throws IOException
	 */
    public static int LeituraInteiro() throws IOException {
        BufferedReader d = new BufferedReader(new InputStreamReader(System.in));
        String stropcao = d.readLine();
        try {
            return java.lang.Integer.parseInt(stropcao);
        } catch (Exception e) {
            return LeituraInteiro();
        }
    }

    /**
	 * Para ler strings
	 * @return
	 * @throws IOException
	 */
    public static String LeituraString() throws IOException {
        BufferedReader d = new BufferedReader(new InputStreamReader(System.in));
        return d.readLine();
    }

    /**
	 * Apresenta o menu de login, perguntando se o utilizador � membro ou n�o
	 * @param rbib
	 * @return O membro escolhido ou null caso n�o seja membro
	 * @throws IOException
	 */
    public static IMembro MenuMembro(IBiblioteca rbib) throws IOException {
        return MenuMembro(rbib, true, false);
    }

    /**
	 * Apresenta o menu de login, perguntando se o utilizador � membro ou n�o
	 * @param rbib
	 * @param pergunta_se_membro - Se true, pergunta se � membro
	 * @param pesq_agrupamento - Pergunta se quer pesquisar o membro no agrupamento todo
	 * @return O membro escolhido ou null caso n�o seja membro
	 * @throws IOException
	 */
    public static IMembro MenuMembro(IBiblioteca rbib, boolean pergunta_se_membro, boolean pesq_agrupamento) throws IOException {
        int opcao = 1;
        IMembro membro = null;
        if (pergunta_se_membro) {
            Menu.Titulo("� membro?");
            Menu.Opcao("1", "Sim");
            Menu.Opcao("2", "N�o");
            opcao = Menu.LeituraInteiro();
        }
        if (opcao == 1) {
            Menu.Titulo("Qual � o membro?");
            Menu.Texto("Escreva palavras para procura:");
            String nome = Menu.LeituraString();
            Vector<IMembro> membros = rbib.PesquisarMembros(nome);
            membro = EscolherMembro(membros);
            if (pesq_agrupamento && membro == null) {
                int opcao2;
                Menu.Titulo("Pesquisar em todo o agrupamento?");
                Menu.Opcao("1", "Sim");
                Menu.Opcao("2", "N�o");
                opcao2 = Menu.LeituraInteiro();
                if (opcao2 == 1) {
                    membros = rbib.PesquisarMembrosAgrupamento(nome);
                    membro = EscolherMembro(membros, true);
                }
            }
        }
        return membro;
    }

    public static ITitulo MenuTitulo(IBiblioteca rbib) throws IOException {
        return MenuTitulo(rbib, Consts.PESQ_OP_AGRUPAMENTO, true);
    }

    public static ITitulo MenuTitulo(IBiblioteca rbib, int tipo_pesquisa) throws IOException {
        return MenuTitulo(rbib, tipo_pesquisa, true);
    }

    /**
	 * Apresenta o menu de escolha de um titulo, permitindo efectuar pesquisa
	 * @param rbib
	 * @param pesq_agrupamento - Faz ou n�o a pergunta para verificar no agrupamento
	 * @param ver_ficha - Visualiza��o da ficha complecta de titulo
	 * @return O titulo escolhido ou null caso n�o tenha sido escolhido nenhum
	 * @throws IOException
	 */
    public static ITitulo MenuTitulo(IBiblioteca rbib, int tipo_pesquisa, boolean ver_ficha) throws IOException {
        Menu.Titulo("Que livro que pretende?");
        Menu.Texto("Escreva palavras para procura:");
        String inquerito = Menu.LeituraString();
        Vector<ITitulo> titulos = null;
        ITitulo titulo = null;
        if (tipo_pesquisa == Consts.PESQ_NORMAL || tipo_pesquisa == Consts.PESQ_OP_AGRUPAMENTO) {
            titulos = rbib.PesquisarTitulos(inquerito);
            titulo = EscolherTitulo(titulos);
        }
        if ((tipo_pesquisa == Consts.PESQ_OP_AGRUPAMENTO && titulo == null) || tipo_pesquisa == Consts.PESQ_AGRUPAMENTO) {
            int opcao = 0;
            if (tipo_pesquisa == Consts.PESQ_OP_AGRUPAMENTO) {
                Menu.Titulo("Pesquisar em todo o agrupamento?");
                Menu.Opcao("1", "Sim");
                Menu.Opcao("2", "N�o");
                opcao = Menu.LeituraInteiro();
            }
            if ((tipo_pesquisa == Consts.PESQ_OP_AGRUPAMENTO && opcao == 1) || tipo_pesquisa == Consts.PESQ_AGRUPAMENTO) {
                titulos = rbib.PesquisarTitulosAgrupamento(inquerito);
                titulo = EscolherTitulo(titulos);
            }
        }
        if (ver_ficha && titulo != null) System.out.println(titulo.VerFicha());
        return titulo;
    }

    public static ICopia MenuCopia(ITitulo titulo) throws IOException {
        Vector<ICopia> copias = titulo.getCopias();
        ICopia copia = EscolherCopia(copias);
        return copia;
    }

    public static ICatalogo MenuCatalogo(IBiblioteca rbib) throws IOException {
        Vector<ICatalogo> catalogos = rbib.getCatalogos();
        ICatalogo catalogo = EscolherCatalogo(catalogos);
        return catalogo;
    }

    public static void MenuValoresCriterio(ITitulo titulo) throws IOException {
        Vector<ICriterio> criterios = titulo.getCatalogo().getCriterios();
        Menu.Titulo("Preencha os seguintes crit�rios");
        for (ICriterio criterio : criterios) {
            Menu.Texto(criterio.getNome() + " :");
            String resposta = Menu.LeituraString();
            titulo.AdicionarValorCriterio(criterio, resposta);
        }
    }

    public static ICriterio MenuCriterio(IBiblioteca rbib) throws IOException {
        Vector<ICriterio> criterios = rbib.getCriterios();
        ICriterio criterio = EscolherCriterio(criterios);
        return criterio;
    }

    /**
	 * Menu para fazer reserva
	 * @param biblioteca - Objecto biblioteca
	 * @param titulo - Titulo (se f�r enviado null, ele faz pesquisa de titulos)
	 * @param membro - Membro (se f�r enviado null, ele faz pesquisa de membros)
	 * @param emprestimo - Indica se a reserva � para empr�stimo ou n�o (consulta)
	 * @return
	 * @throws IOException
	 */
    public static void MenuReserva(IBiblioteca biblioteca, ITitulo titulo, IMembro membro, boolean emprestimo) throws IOException {
        Titulo("Pretende reservar o titulo?");
        Opcao(1, "Sim");
        Opcao(2, "N�o");
        if (Menu.LeituraInteiro() == 1) {
            IReserva reserva;
            if (titulo == null) titulo = MenuTitulo(biblioteca, Consts.PESQ_NORMAL, true);
            if (emprestimo) {
                if (titulo.ExisteCopiaRegularDisponivel(membro)) {
                    Erro("N�o se podem fazer reservas quando existem c�pias regulares dispon�veis para empr�stimo.");
                    return;
                }
            } else if (titulo.ExisteCopiaDisponivel(membro)) {
                Erro("N�o se podem fazer reservas quando existem c�pias dispon�veis para consulta.");
                return;
            }
            reserva = titulo.ExisteReserva(membro);
            if (reserva != null) {
                Erro("J� existe uma reserva efectuada na data " + FormatarData(reserva.getData()) + " pelo membro para o titulo em quest�o.");
                return;
            }
            if (membro == null) membro = MenuMembro(biblioteca, false, true);
            reserva = titulo.Reservar(membro);
            if (reserva != null) Aviso("Reserva efectuada com sucesso! Ser� notificado quando existir uma c�pia dispon�vel."); else Erro("A reserva n�o p�de ser efectuada.");
        }
    }

    /**
	 * Pergunta se pretende enviar uma c�pia para a biblioteca
	 * @param biblioteca
	 * @param titulo
	 * @param membro
	 * @param emprestimo
	 * @throws IOException
	 */
    public static void MenuEnvioCopia(IBiblioteca biblioteca, ITitulo titulo, IMembro membro, boolean emprestimo) throws IOException {
        if (membro != null && biblioteca != null && titulo != null) {
            Titulo("Pretende pedir o envio da c�pia para esta bibliotca?");
            Opcao(1, "Sim");
            Opcao(2, "N�o");
            if (Menu.LeituraInteiro() == 1) {
                if (titulo.EnviarCopiaDisponivel(membro, biblioteca, emprestimo) != null) Aviso("A c�pia ir� ser enviada para esta biblioteca. Ficou assim reservada."); else Erro("O envio da c�pia n�o � poss�vel.");
            }
        }
    }

    /**
	 * D� um menu ao cliente para escolha de membro
	 * @param membros - Vector com membros para escolha
	 * @return Membro que o cliente escolher
	 * @throws IOException
	 */
    private static IMembro EscolherMembro(Vector<IMembro> membros) throws IOException {
        return EscolherMembro(membros, false);
    }

    /**
	 * D� um menu ao cliente para escolha de membro
	 * @param membros - Vector com membros para escolha
	 * @param mostra_biblioteca - Mostra a biblioteca na lista
	 * @return Membro que o cliente escolher
	 * @throws IOException
	 */
    private static IMembro EscolherMembro(Vector<IMembro> membros, boolean mostra_biblioteca) throws IOException {
        if (membros == null) {
            Menu.Erro("O vector enviado para EscolherMembro encontra-se a null");
            return null;
        }
        if (membros.size() == 0) {
            Menu.Erro("N�o existem membros para a pequisa efectuada");
            return null;
        }
        Menu.Texto("Escolha um membro da lista:");
        int i = 0;
        for (IMembro membro : membros) {
            i++;
            String opstr = membro.toStr();
            if (mostra_biblioteca) opstr += "(" + membro.getBiblioteca().getNome() + ")";
            Menu.Opcao((i), opstr);
        }
        Menu.Opcao("0", "Nenhum");
        int opcao = Menu.LeituraInteiro();
        if (opcao == 0 || opcao - 1 < 0 || opcao - 1 >= membros.size()) return null;
        return membros.get(opcao - 1);
    }

    /**
	 * D� um menu ao cliente para escolha do titulo
	 * @param titulos - Vector com titulos para escolha
	 * @return Titulo que o cliente escolher
	 * @throws IOException
	 */
    private static ITitulo EscolherTitulo(Vector<ITitulo> titulos) throws IOException {
        if (titulos == null) {
            Menu.Erro("O vector enviado para EscolherTitulo encontra-se a null");
            return null;
        }
        if (titulos.size() == 0) {
            Menu.Erro("N�o existem titulos para a pequisa efectuada");
            return null;
        }
        Menu.Texto("Escolha um titulo da lista:");
        int i = 0;
        for (ITitulo titulo : titulos) {
            i++;
            Menu.Opcao((i), titulo.toStr());
        }
        Menu.Opcao("0", "Nenhum");
        int opcao = Menu.LeituraInteiro();
        if (opcao == 0 || opcao - 1 < 0 || opcao - 1 >= titulos.size()) return null;
        return titulos.get(opcao - 1);
    }

    /**
	 * D� um menu ao cliente para escolha do titulo
	 * @param titulos - Vector com titulos para escolha
	 * @return Titulo que o cliente escolher
	 * @throws IOException
	 */
    private static ICopia EscolherCopia(Vector<ICopia> copias) throws IOException {
        if (copias == null) {
            Menu.Erro("O vector enviado para EscolherCopia encontra-se a null");
            return null;
        }
        if (copias.size() == 0) {
            Menu.Erro("N�o existem c�pias para a pequisa efectuada");
            return null;
        }
        Menu.Texto("Escolha uma c�pia da lista:");
        int i = 0;
        for (ICopia copia : copias) {
            i++;
            Menu.Opcao((i), copia.toStr());
        }
        Menu.Opcao("0", "Nenhum");
        int opcao = Menu.LeituraInteiro();
        if (opcao == 0 || opcao - 1 < 0 || opcao - 1 >= copias.size()) return null;
        return copias.get(opcao - 1);
    }

    /**
	 * D� um menu ao cliente para escolha do titulo
	 * @param titulos - Vector com titulos para escolha
	 * @return Titulo que o cliente escolher
	 * @throws IOException
	 */
    private static ICatalogo EscolherCatalogo(Vector<ICatalogo> catalogos) throws IOException {
        if (catalogos == null) {
            Menu.Erro("O vector enviado para EscolherCatalogo encontra-se a null");
            return null;
        }
        if (catalogos.size() == 0) {
            Menu.Erro("N�o existem catalogos");
            return null;
        }
        Menu.Texto("Escolha um cat�logo da lista:");
        int i = 0;
        for (ICatalogo catalogo : catalogos) {
            i++;
            Menu.Opcao((i), catalogo.toStr());
        }
        Menu.Opcao("0", "Nenhum");
        int opcao = Menu.LeituraInteiro();
        if (opcao == 0 || opcao - 1 < 0 || opcao - 1 >= catalogos.size()) return null;
        return catalogos.get(opcao - 1);
    }

    /**
	 * Menu de escolha de crit�rios
	 */
    private static ICriterio EscolherCriterio(Vector<ICriterio> criterios) throws IOException {
        if (criterios == null) {
            Menu.Erro("O vector enviado para EscolherCriterio encontra-se a null");
            return null;
        }
        if (criterios.size() == 0) {
            Menu.Erro("N�o existem criterios");
            return null;
        }
        Menu.Texto("Escolha um crit�rio da lista:");
        int i = 0;
        for (ICriterio criterio : criterios) {
            i++;
            Menu.Opcao((i), criterio.toStr());
        }
        Menu.Opcao("0", "Nenhum");
        int opcao = Menu.LeituraInteiro();
        if (opcao == 0 || opcao - 1 < 0 || opcao - 1 >= criterios.size()) return null;
        return criterios.get(opcao - 1);
    }

    public static void VerNotificacoes(IMembro membro) {
        try {
            Vector<INotificacao> notificacoes = membro.getNotificacoes();
            Menu.Titulo("Notifica��es");
            Menu.Separador("Ver Notifica��es");
            int i = 1;
            for (INotificacao notificacao : notificacoes) {
                Menu.Opcao(i, notificacao.toStr());
                i++;
            }
            Menu.Texto("Prima em qualquer tecla para continuar");
            Menu.LeituraString();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String FormatarData(Date data) {
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return df.format(data);
    }
}
