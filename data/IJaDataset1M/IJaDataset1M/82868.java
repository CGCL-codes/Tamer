package at.jku.xlwrap.map.transf;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import at.jku.xlwrap.common.XLWrapException;
import at.jku.xlwrap.exec.ExecutionContext;
import at.jku.xlwrap.exec.TransformationStage;
import at.jku.xlwrap.map.range.Range;

/**
 * @author dorgon
 *
 */
public class FileRepeat extends TransformationBase {

    private List<String> targetFiles;

    /**
	 * 
	 * @param targetFiles
	 * @param restriction
	 * @param skipCondition
	 * @param breakCondition
	 * @throws XLWrapException 
	 */
    public FileRepeat(String targetFiles, String restriction, String skipCondition, String breakCondition) throws XLWrapException {
        super(restriction, skipCondition, breakCondition);
        this.targetFiles = new ArrayList<String>();
        StringTokenizer tok = new StringTokenizer(targetFiles, ",");
        String next;
        while (tok.hasMoreTokens()) {
            next = tok.nextToken().trim();
            next = next.replaceAll("['\"]", "");
            this.targetFiles.add(next);
        }
    }

    @Override
    public String getArgsAsString() {
        return "repeat for " + targetFiles.size() + " files: " + targetFiles.toString();
    }

    /**
	 * @return the targetFiles
	 */
    public List<String> getTargetFiles() {
        return targetFiles;
    }

    @Override
    public TransformationStage getExecutor(ExecutionContext context) {
        return new TransformationStage(context) {

            private int index;

            private String file;

            @Override
            public void init() {
                index = 0;
                file = null;
            }

            @Override
            public boolean hasMoreTransformations() throws XLWrapException {
                if (index < targetFiles.size()) {
                    file = targetFiles.get(index++);
                    return true;
                } else return false;
            }

            @Override
            public Range transform(Range range, Range restriction) throws XLWrapException {
                return range.changeFileName(file, restriction, context);
            }

            @Override
            public String getThisStatus() {
                return "FileRepeat: " + file + "( " + (index + 1) + "/" + targetFiles.size();
            }
        };
    }
}
