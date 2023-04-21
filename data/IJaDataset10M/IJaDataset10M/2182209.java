package tp1POO;

import tp1POO.Persistencia.GerenciadorObjetos;
import tp1POO.Persistencia.Pesquisa;
import tp1POO.Modelo.*;
import GUI.*;
import java.util.Vector;

public class Principal {

    public static void main(String args[]) {
        Vector<ObjetoPOO> vet = GerenciadorObjetos.obterInstancia().obterObjs(new Funcionario(Funcionario.Cargo.Administrador));
        if (vet.size() == 0) {
            Funcionario func = new Funcionario("admin", "12312312312", Funcionario.Cargo.Administrador);
            func.setSenha("123");
            func.setLogin("admin");
            func.setTelefone("12312");
            GerenciadorObjetos.obterInstancia().salvarObj(func);
        }
        GestorLog.obterInstancia().inserirMsgLog("SISTEMA INICIALIZADO\n");
        GuiCreator.obterInstancia().criarGuiPrincipal();
    }
}
