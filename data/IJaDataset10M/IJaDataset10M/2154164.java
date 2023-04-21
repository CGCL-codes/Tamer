package screen.tools.sbs.cmake.writers;

import java.io.IOException;
import java.io.Writer;
import screen.tools.sbs.cmake.CMakeSegmentWriter;
import screen.tools.sbs.cmake.CMakePack;

/**
 * write filter for header files
 * <pre>
 * FILE(
 *     GLOB_RECURSE
 *     INC_FILES
 *     include/*.hpp
 *     include/*.h
 *     include/*.inl
 *     include/*.tpp
 *     include/*.i
 * )
 * </pre>
 * 
 * @author Ratouit Thomas
 *
 */
public class CMakeHeaderFilterWriter implements CMakeSegmentWriter {

    /**
	 * @see screen.tools.sbs.cmake.CMakeSegmentWriter#write(screen.tools.sbs.cmake.CMakePack, java.io.Writer)
	 */
    public void write(CMakePack cmakePack, Writer cmakeListsWriter) throws IOException {
        if (!cmakePack.isTest()) {
            cmakeListsWriter.write("FILE(\n");
            cmakeListsWriter.write("    GLOB_RECURSE\n");
            cmakeListsWriter.write("    INC_FILES\n");
            cmakeListsWriter.write("    include/*.hpp\n");
            cmakeListsWriter.write("    include/*.h\n");
            cmakeListsWriter.write("    include/*.inl\n");
            cmakeListsWriter.write("    include/*.tpp\n");
            cmakeListsWriter.write("    include/*.i\n");
            cmakeListsWriter.write(")\n");
        }
    }
}
