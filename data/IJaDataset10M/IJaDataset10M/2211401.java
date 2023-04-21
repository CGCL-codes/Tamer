package fr.insee.rome.io.writer;

import java.io.IOException;
import java.util.Collection;
import fr.insee.rome.bean.Metier;

public interface RomeWriter {

    void open(String path) throws IOException;

    void write(Collection<Metier> metiers) throws IOException;

    void close() throws IOException;
}
