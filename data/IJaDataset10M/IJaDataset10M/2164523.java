package corujao.model;

import corujao.ApplicationException;
import corujao.Util;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 *
 * @author Eduardo_Rangel
 */
public class Monitor {

    private int id;

    private String nome;

    private String classe;

    private String xslTexto;

    /** Creates a new instance of Monitor */
    public Monitor() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getXslTexto() {
        return xslTexto;
    }

    public void setXslTexto(String xslTexto) {
        this.xslTexto = xslTexto;
    }

    public String getClasse() {
        return classe;
    }

    public void setClasse(String classe) {
        this.classe = classe;
    }

    public void serialize(DataOutputStream dataStream) throws ApplicationException {
        try {
            dataStream.writeInt(id);
            dataStream.writeUTF(Util.encNull(classe));
            dataStream.writeUTF(Util.encNull(nome));
            dataStream.writeUTF(Util.encNull(xslTexto));
        } catch (IOException ioe) {
            throw new ApplicationException(ioe);
        }
    }

    public static Monitor deserialize(DataInputStream dataStream) throws ApplicationException {
        try {
            Monitor monitor = new Monitor();
            monitor.setId(dataStream.readInt());
            monitor.setClasse(Util.decNull(dataStream.readUTF()));
            monitor.setNome(Util.decNull(dataStream.readUTF()));
            monitor.setXslTexto(Util.decNull(dataStream.readUTF()));
            return monitor;
        } catch (IOException ioe) {
            throw new ApplicationException(ioe);
        }
    }
}
