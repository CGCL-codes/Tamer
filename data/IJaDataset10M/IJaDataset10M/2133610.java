package same.alg;

public interface ISourceFilter {

    public String filter(String a_line);

    public ISourceFilter copy();
}
