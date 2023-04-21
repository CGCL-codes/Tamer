package common.utilities.fp;

/**
 * Example:
 * <pre>
 *   UnaryFunction<String, File> getFileNames = new UnaryFunction<String, File>() {
 *       public String eval(File file) {
 *           return file.getName();
 *       }
 *   };
 *
 *   List<String> fileNames = map(getFileNames, listOfFiles);
 * <pre>
 *
 * @see FP#forEach(UnaryFunction, java.util.List)
 * @see FP#collect(UnaryFunction, java.util.List)
 *
 * @author Nicolas Cabanis
 */
public interface UnaryFunction<R, T> {

    public R eval(T t);
}
