package net.sourceforge.sctmf.model.services.salvar;

import net.sourceforge.sctmf.model.pojo.AFD;
import net.sourceforge.sctmf.model.pojo.AFMV;
import net.sourceforge.sctmf.model.pojo.AFND;
import net.sourceforge.sctmf.model.pojo.AP;
import net.sourceforge.sctmf.model.pojo.ER;
import net.sourceforge.sctmf.model.pojo.Estado;
import net.sourceforge.sctmf.model.pojo.GLC;
import net.sourceforge.sctmf.model.pojo.MT;
import net.sourceforge.sctmf.model.pojo.ModeloFormal;
import net.sourceforge.sctmf.model.pojo.RegraProducao;
import net.sourceforge.sctmf.model.pojo.Simbolo;
import net.sourceforge.sctmf.model.pojo.Transicao;
import net.sourceforge.sctmf.model.pojo.TransicaoAP;
import net.sourceforge.sctmf.model.pojo.TransicaoMT;
import java.io.File;
import java.io.FileWriter;

/**
 *
 * @author rafael2009_00
 */
public class SalvarModeloFormal implements Salvar {

    private FileWriter fw;

    private File file;

    /**
     * Creates a new instance of Salvar
     */
    public SalvarModeloFormal() {
    }

    public void salvar(File arquivo, ModeloFormal mf) throws SalvarException {
        this.file = arquivo;
        if (mf instanceof AP) this.salvarAP((AP) mf); else if (mf instanceof AFMV) this.salvarAFMV((AFMV) mf); else if (mf instanceof AFD) this.salvarAFD((AFD) mf); else if (mf instanceof AFND) this.salvarAFND((AFND) mf); else if (mf instanceof ER) this.salvarER((ER) mf); else if (mf instanceof GLC) this.salvarGLC((GLC) mf); else if (mf instanceof MT) this.salvarMT((MT) mf);
    }

    private void salvarAFD(AFD afd) throws SalvarException {
        StringBuilder sb = new StringBuilder();
        sb.append("E:");
        for (Simbolo s : afd.getSimbolos()) sb.append(s.getNome() + "-");
        sb.append("\nS:");
        for (Estado e : afd.getEstados()) sb.append(e.getNome() + "-");
        Estado aux = afd.getEstadoInicial();
        sb.append("\nI:");
        if (aux != null) sb.append(aux.getNome());
        sb.append("\nF:");
        for (Estado e : afd.getEstadosFinais()) sb.append(e.getNome() + "-");
        for (Transicao t : afd.getTransicoes()) sb.append("\nT:" + t.getEstOri().getNome() + "-" + t.getSimbolo().getNome() + "-" + t.getEstDest().getNome());
        try {
            this.writeInFile(new File(this.file.getPath() + ".afd"), sb.toString());
            this.showOkMessage("AFD");
        } catch (Exception ioex) {
            throw new SalvarException("Erro ao Salvar AFD");
        }
    }

    private void salvarAFND(AFND afnd) throws SalvarException {
        StringBuilder sb = new StringBuilder();
        sb.append("E:");
        for (Simbolo s : afnd.getSimbolos()) sb.append(s.getNome() + "-");
        sb.append("\nS:");
        for (Estado e : afnd.getEstados()) sb.append(e.getNome() + "-");
        sb.append("\nI:");
        for (Estado ei : afnd.getEstadosIniciais()) sb.append(ei.getNome() + "-");
        sb.append("\nF:");
        for (Estado e : afnd.getEstadosFinais()) sb.append(e.getNome() + "-");
        for (Transicao t : afnd.getTransicoes()) sb.append("\nT:" + t.getEstOri().getNome() + "-" + t.getSimbolo().getNome() + "-" + t.getEstDest().getNome());
        try {
            this.writeInFile(new File(this.file.getPath() + ".afnd"), sb.toString());
            this.showOkMessage("AFND");
        } catch (Exception ioex) {
            throw new SalvarException("Erro ao Salvar AFND");
        }
    }

    private void salvarER(ER er) throws SalvarException {
        StringBuilder sb = new StringBuilder();
        sb.append("E:");
        for (Simbolo s : er.getAlfabeto()) sb.append(s.getNome() + "-");
        sb.append("\nR:").append(er.getExpressaoRegular());
        try {
            this.writeInFile(new File(this.file.getPath() + ".er"), sb.toString());
            this.showOkMessage("ER");
        } catch (Exception ioex) {
            throw new SalvarException("Erro ao Salvar AFND");
        }
    }

    private void salvarAFMV(AFMV afmv) throws SalvarException {
        StringBuilder sb = new StringBuilder();
        sb.append("E:");
        for (Simbolo s : afmv.getSimbolos()) sb.append(s.getNome() + "-");
        sb.append("\nS:");
        for (Estado e : afmv.getEstados()) sb.append(e.getNome() + "-");
        Estado aux = afmv.getEstadoInicial();
        sb.append("\nI:");
        if (aux != null) sb.append(aux.getNome());
        sb.append("\nF:");
        for (Estado e : afmv.getEstadosFinais()) sb.append(e.getNome() + "-");
        for (Transicao t : afmv.getTransicoes()) sb.append("\nT:" + t.getEstOri().getNome() + "-" + t.getSimbolo().getNome() + "-" + t.getEstDest().getNome());
        try {
            this.writeInFile(new File(this.file.getPath() + ".afmv"), sb.toString());
            this.showOkMessage("AFMV");
        } catch (Exception ioex) {
            throw new SalvarException("Erro ao Salvar AFMV");
        }
    }

    private void salvarAP(AP ap) throws SalvarException {
        StringBuilder sb = new StringBuilder();
        sb.append("E:");
        for (Simbolo s : ap.getSimbolos()) sb.append(s.getNome() + "-");
        sb.append("\nP:");
        for (Simbolo s : ap.getSimbolosPilha()) sb.append(s.getNome() + "-");
        sb.append("\nS:");
        for (Estado e : ap.getEstados()) sb.append(e.getNome() + "-");
        Estado aux = ap.getEstadoInicial();
        sb.append("\nI:");
        if (aux != null) sb.append(aux.getNome());
        Simbolo s = ap.getTopoPilha();
        sb.append("\nB:");
        if (s != null) sb.append(s.getNome());
        for (TransicaoAP t : ap.getTransicoesAP()) {
            sb.append("\nT:");
            sb.append(t.getEstOri().getNome());
            sb.append("-");
            sb.append(t.getSimbolo().getNome() == 'λ' ? "?" : t.getSimbolo().getNome());
            sb.append("-");
            sb.append(t.getSimBasePilha().getNome());
            sb.append("-");
            sb.append(t.getEstDest().getNome());
            sb.append("-");
            for (Simbolo x : t.getEntradaPilha()) sb.append(x.getNome());
        }
        try {
            this.writeInFile(new File(this.file.getPath() + ".ap"), sb.toString());
            this.showOkMessage("AP");
        } catch (Exception ioex) {
            throw new SalvarException("Erro ao Salvar AP");
        }
    }

    private void salvarGLC(GLC glc) throws SalvarException {
        StringBuilder sb = new StringBuilder();
        sb.append("V:");
        for (Simbolo s : glc.getSimbNTerm()) sb.append(s.getNome() + "-");
        sb.append("\nT:");
        for (Simbolo s : glc.getSimbTerm()) sb.append(s.getNome() + "-");
        Simbolo aux = glc.getSimbInicial();
        sb.append("\nS:");
        if (aux != null) sb.append(aux.getNome());
        for (RegraProducao rp : glc.getRegrasProducao()) {
            sb.append("\nP:" + rp.getSimbLEsq().getNome()).append("-");
            for (Simbolo x : rp.getSimbLDireito()) sb.append(x.getNome());
        }
        try {
            this.writeInFile(new File(this.file.getPath() + ".glc"), sb.toString());
            this.showOkMessage("GLC");
        } catch (Exception ioex) {
            throw new SalvarException("Erro ao Salvar GLC");
        }
    }

    private void salvarMT(MT mt) throws SalvarException {
        StringBuilder sb = new StringBuilder();
        sb.append("E:");
        for (Simbolo s : mt.getAlfabeto()) sb.append(s.getNome() + "-");
        sb.append("\nV:");
        for (Simbolo s : mt.getAlfabetoAux()) sb.append(s.getNome() + "-");
        sb.append("\nQ:");
        for (Estado e : mt.getEstados()) sb.append(e.getNome() + "-");
        sb.append("\nI:");
        Estado aux = mt.getEstIni();
        if (aux != null) sb.append(aux.getNome());
        sb.append("\nF:");
        for (Estado e : mt.getEstFinais()) sb.append(e.getNome() + "-");
        for (TransicaoMT t : mt.getTransicoes()) {
            sb.append("\nT:").append(t.getEstAtual().getNome()).append("-").append(t.getSimLido().getNome()).append("-").append(t.getEstDestino().getNome()).append("-").append(t.getSimbEscrito().getNome()).append("-").append(t.getDirecao());
        }
        try {
            this.writeInFile(new File(this.file.getPath() + ".mt"), sb.toString());
            this.showOkMessage("MT");
        } catch (Exception ioex) {
            throw new SalvarException("Erro ao Salvar MT");
        }
    }

    private void writeInFile(File file, String content) throws Exception {
        FileWriter fw = new FileWriter(file);
        fw.write(content);
        fw.flush();
        fw.close();
    }

    private void showOkMessage(String nameFormalModel) {
        javax.swing.JOptionPane.showMessageDialog(null, nameFormalModel + " Salvo com Sucesso", "Operação Concluída", javax.swing.JOptionPane.INFORMATION_MESSAGE);
    }
}
