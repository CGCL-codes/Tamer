package sampledata.la.data.ksa00;

import cef.pui.pob.Builder;
import cef.pui.pob.BuilderException;
import commons.data.DNData;
import commons.provider.ProviderException;
import commons.provider.ProviderManager;
import commons.provider.Providers.ModelDataProvider;
import genomemap.cef.pui.pob.Builders;
import genomemap.data.KSA00Data;
import genomemap.model.KSA00;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @since
 * @author Susanta Tewari
 */
public class KSA00DNData extends DNData {

    private ModelDataProvider<KSA00, KSA00Data> provider;

    private Builder<KSA00Data> dataBuilder = Builders.getKSA00DataBuilder(null);

    public KSA00DNData() {
        this.name = "sample-ksa00-data-1";
        this.displayName = this.name;
        this.shortDescription = "Sample data set (1) for model KSA00";
        try {
            this.provider = ProviderManager.getProvider("impl1", ModelDataProvider.class);
        } catch (ProviderException ex) {
            Logger.getLogger(KSA00DNData.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public KSA00DNData(String name, String displayName, String shortDescription, ModelDataProvider<KSA00, KSA00Data> provider) {
        this.name = name;
        this.displayName = displayName;
        this.shortDescription = shortDescription;
        this.provider = provider;
    }

    @Override
    public Builder getBuilder() throws BuilderException {
        dataBuilder.setProvider(provider);
        return dataBuilder;
    }

    @Override
    public Class<? extends Builder> getBuilderClass() {
        return dataBuilder.getClass();
    }
}
