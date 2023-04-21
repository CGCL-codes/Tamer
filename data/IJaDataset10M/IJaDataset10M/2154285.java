package lv.odylab.evemanage.client.event.priceset;

import lv.odylab.evemanage.client.EveManageConstants;
import lv.odylab.evemanage.client.rpc.action.priceset.PriceSetRenameActionResponse;
import lv.odylab.evemanage.client.rpc.dto.priceset.PriceSetDto;
import lv.odylab.evemanage.client.rpc.dto.priceset.PriceSetNameDto;
import lv.odylab.evemanage.client.tracking.TrackingManager;
import java.util.List;

public class PriceSetRenamedEvent extends PriceSetTabEvent<PriceSetRenamedEventHandler> {

    public static final Type<PriceSetRenamedEventHandler> TYPE = new Type<PriceSetRenamedEventHandler>();

    private List<PriceSetNameDto> priceSetNames;

    private PriceSetDto priceSet;

    private Integer currentPriceSetNameIndex;

    public PriceSetRenamedEvent(TrackingManager trackingManager, EveManageConstants constants, PriceSetRenameActionResponse response, Long msDuration) {
        super(trackingManager, constants, msDuration);
        this.priceSetNames = response.getPriceSetNames();
        this.priceSet = response.getPriceSet();
        this.currentPriceSetNameIndex = response.getCurrentPriceSetNameIndex();
    }

    @Override
    public Type<PriceSetRenamedEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(PriceSetRenamedEventHandler handler) {
        handler.onPriceSetRenamed(this);
        trackEvent(priceSet.getName());
    }

    public List<PriceSetNameDto> getPriceSetNames() {
        return priceSetNames;
    }

    public PriceSetDto getPriceSet() {
        return priceSet;
    }

    public Integer getCurrentPriceSetNameIndex() {
        return currentPriceSetNameIndex;
    }
}
