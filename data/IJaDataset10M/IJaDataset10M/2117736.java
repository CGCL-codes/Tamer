package rs.mgifos.mosquito;

import java.util.Properties;
import rs.mgifos.mosquito.model.MetaModel;

/**
 * Creates and loads meta-model (<code>rs.mgifos.mosquito.model.MetaModel</code>).
 * 
 * @author <a href="mailto:nikola.petkov@gmail.com">Nikola Petkov
 *         &lt;nikola.petkov@gmail.com&gt;</a>
 */
public interface IMetaLoader {

    /**
	 * @return
	 * @throws LoadingException
	 */
    MetaModel getMetaModel(Properties config) throws LoadingException;
}
