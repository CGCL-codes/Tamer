package net.sf.brightside.mobilestock.service.hibernate.nonstandard;

import java.util.List;
import net.sf.brightside.mobilestock.core.spring.BeansProvider;
import net.sf.brightside.mobilestock.core.spring.CommandsProvider;
import net.sf.brightside.mobilestock.core.spring.IBeansProvider;
import net.sf.brightside.mobilestock.core.spring.ICommandsProvider;
import net.sf.brightside.mobilestock.metamodel.Broker;
import net.sf.brightside.mobilestock.metamodel.InsufficientShareAmountException;
import net.sf.brightside.mobilestock.metamodel.Ownership;
import net.sf.brightside.mobilestock.metamodel.Shareholder;
import net.sf.brightside.mobilestock.service.AccountDoesntExistException;
import net.sf.brightside.mobilestock.service.IGetByType;
import net.sf.brightside.mobilestock.service.IListSharesFromShareholder;
import net.sf.brightside.mobilestock.service.IPurchaseShare;
import net.sf.brightside.mobilestock.service.ShareDoesntExistException;

public class PurchaseShareImplTest {

    /**
	 * @param args
	 * @throws ShareDoesntExistException 
	 * @throws AccountDoesntExistException 
	 * @throws InsufficientShareAmountException 
	 */
    public static void main(String[] args) throws InsufficientShareAmountException, AccountDoesntExistException, ShareDoesntExistException {
        IBeansProvider provider = new BeansProvider();
        ICommandsProvider cProvider = new CommandsProvider();
        IPurchaseShare share = provider.getBean(IPurchaseShare.class);
        IGetByType getCompCmd = cProvider.getGetCommand();
        List<Shareholder> parties = getCompCmd.getByType(Shareholder.class);
        IListSharesFromShareholder isfp = provider.getBean(IListSharesFromShareholder.class);
        List<Ownership> shares = isfp.list(parties.get(1));
        Ownership saToPurchase = provider.getBean(Ownership.class);
        saToPurchase.setShare(shares.get(0).getShare());
        saToPurchase.setAmount(10);
        IGetByType getBrokerCmd = cProvider.getGetCommand();
        List<Broker> brokers = getBrokerCmd.getByType(Broker.class);
        share.purchase(saToPurchase.getShare(), saToPurchase.getAmount(), parties.get(1), parties.get(0), brokers.get(0));
    }
}
