package simulatorKit.simulation.station;

import java.util.Properties;

/**
 * @author igaler
 *
 */
public class TorrentModJuliaStationFactory extends AbstractStationFactory {

    private static TorrentModJuliaStationFactory instance;

    protected TorrentModJuliaStationFactory() {
    }

    public static TorrentModJuliaStationFactory getInstance() {
        if (instance == null) instance = new TorrentModJuliaStationFactory();
        return instance;
    }

    @Override
    public IUncompletedStation getPeerInstance(short nodeIndex) throws Exception {
        this.updateTopology(nodeIndex);
        return new TorrentModJuliaPeer(nodeIndex);
    }

    @Override
    public ICompletedStation getSeedInstance(short nodeIndex) throws Exception {
        this.updateTopology(nodeIndex);
        return new TorrentModJuliaSeed(nodeIndex);
    }

    public void setStationParameters(Properties params) {
        super.setStationParameters(params);
        TorrentModJuliaStation.setParameters(params);
    }

    @Override
    public ICompletedStation getSeedInstance(IUncompletedStation peer) throws Exception {
        return new TorrentModJuliaSeed((TorrentModJuliaPeer) peer);
    }
}
