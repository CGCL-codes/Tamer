package br.com.tetisdb.query;

import br.com.tetisdb.exception.TetisAttributeNotFoundException;
import java.util.List;

public interface TetisQuery {

    public Attribute addAttribute(String attributeName) throws TetisAttributeNotFoundException;

    public List execute() throws Exception;
}
