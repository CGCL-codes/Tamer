package maze.simple_assembler;

/**
 * @author Normunds Mazurs (MAZE)
 * 
 */
public interface Disassembler {

    Disassembler createComplexDisassembler(String name, String type);

    Disassembler primitive(String name, String value);

    Disassembler primitive(String name, Long value);

    Disassembler primitive(String name, Double value);

    Disassembler primitive(String name, Boolean value);
}
