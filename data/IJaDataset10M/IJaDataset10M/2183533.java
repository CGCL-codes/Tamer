package isg3.data;

import isg3.article.Rate;
import java.util.Collection;

public interface IRateDAO {

    public Collection selectAll(String article);

    public Collection selectAllByOID(String articleOID);

    public boolean insert(Rate r, String article);

    public boolean update(Rate r, String article);

    public Rate select(String article, String user);

    public boolean removeAll(String article);
}
