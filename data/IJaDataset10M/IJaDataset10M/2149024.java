package tp1POO.Persistencia;

import tp1POO.Modelo.Cliente;
import tp1POO.Modelo.ObjetoPOO;
import tp1POO.Modelo.Pessoa;

public class ClienteDAOTexto extends DAOTexto {

    @Override
    public void salvar(ObjetoPOO parObj) {
        Cliente parCliente = (Cliente) parObj;
        if (parCliente == null) return;
        String tmpValue;
        tmpValue = parCliente.getIdentifier() + FileManager.getSeparator() + parCliente.getLocacoes().toString();
        Pessoa pes = new Pessoa();
        pes.copy(parCliente);
        gestor.SalvarObjeto(tmpValue, parCliente.getClass().getSimpleName());
    }

    @Override
    public ObjetoPOO converterObj(String parString) {
        if (parString != null && parString.length() > 0) {
            String s[] = parString.split(FileManager.getSeparator());
            Pessoa pes = (Pessoa) GerenciadorObjetos.obterInstancia().obterObj(new Pessoa(s[0]));
            Cliente cli = new Cliente();
            if (pes != null) cli.copy(pes);
            return cli;
        }
        return null;
    }
}
