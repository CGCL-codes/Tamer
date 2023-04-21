package org.dspace.sword;

import java.util.Date;
import org.apache.log4j.Logger;
import org.dspace.content.Collection;
import org.dspace.content.DSpaceObject;
import org.dspace.content.Item;
import org.dspace.core.Context;
import org.dspace.core.LogManager;
import org.purl.sword.base.Deposit;
import org.purl.sword.base.DepositResponse;
import org.purl.sword.base.SWORDAuthenticationException;
import org.purl.sword.base.SWORDEntry;
import org.purl.sword.base.SWORDErrorException;

/**
 * This class is responsible for initiating the process of 
 * deposit of SWORD Deposit objects into the DSpace repository
 * 
 * @author Richard Jones
 *
 */
public class DepositManager {

    /** Log4j logger */
    public static Logger log = Logger.getLogger(DepositManager.class);

    /** The SWORD service implementation */
    private SWORDService swordService;

    /**
	 * Construct a new DepositManager using the given instantiation of
	 * the SWORD service implementation
	 *
	 * @param service
	 */
    public DepositManager(SWORDService service) {
        this.swordService = service;
        log.debug("Created instance of DepositManager");
    }

    public DSpaceObject getDepositTarget(Deposit deposit) throws DSpaceSWORDException, SWORDErrorException {
        SWORDUrlManager urlManager = swordService.getUrlManager();
        Context context = swordService.getContext();
        String loc = deposit.getLocation();
        DSpaceObject dso = urlManager.getDSpaceObject(context, loc);
        swordService.message("Performing deposit using location: " + loc);
        if (dso instanceof Collection) {
            swordService.message("Location resolves to collection with handle: " + dso.getHandle() + " and name: " + ((Collection) dso).getMetadata("name"));
        } else if (dso instanceof Item) {
            swordService.message("Location resolves to item with handle: " + dso.getHandle());
        }
        return dso;
    }

    /**
	 * Once this object is fully prepared, this method will execute
	 * the deposit process.  The returned DepositRequest can be
	 * used then to assembel the SWORD response.
	 * 
	 * @return	the response to the deposit request
	 * @throws DSpaceSWORDException
	 */
    public DepositResponse deposit(Deposit deposit) throws DSpaceSWORDException, SWORDErrorException, SWORDAuthenticationException {
        Date start = new Date();
        swordService.message("Initialising verbose deposit");
        SWORDContext swordContext = swordService.getSwordContext();
        Context context = swordService.getContext();
        DSpaceObject dso = this.getDepositTarget(deposit);
        SWORDAuthenticator auth = new SWORDAuthenticator();
        if (!auth.canSubmit(swordService, deposit, dso)) {
            String oboEmail = "none";
            if (swordContext.getOnBehalfOf() != null) {
                oboEmail = swordContext.getOnBehalfOf().getEmail();
            }
            log.info(LogManager.getHeader(context, "deposit_failed_authorisation", "user=" + swordContext.getAuthenticated().getEmail() + ",on_behalf_of=" + oboEmail));
            throw new SWORDAuthenticationException("Cannot submit to the given collection with this context");
        }
        swordService.message("Authenticated user: " + swordContext.getAuthenticated().getEmail());
        if (swordContext.getOnBehalfOf() != null) {
            swordService.message("Depositing on behalf of: " + swordContext.getOnBehalfOf().getEmail());
        }
        Depositor dep = null;
        if (dso instanceof Collection) {
            swordService.message("Initialising depositor for an Item in a Collection");
            dep = new CollectionDepositor(swordService, dso);
        } else if (dso instanceof Item) {
            swordService.message("Initialising depositor for a Bitstream in an Item");
            dep = new ItemDepositor(swordService, dso);
        }
        if (dep == null) {
            log.error("The specified deposit target does not exist, or is not a collection or an item");
            throw new DSpaceSWORDException("Deposit target is not a collection or an item");
        }
        DepositResult result = dep.doDeposit(deposit);
        String handle = result.getHandle();
        int state = Deposit.CREATED;
        if (handle == null || "".equals(handle)) {
            state = Deposit.ACCEPTED;
        }
        DepositResponse response = new DepositResponse(state);
        response.setLocation(result.getMediaLink());
        DSpaceATOMEntry dsatom = null;
        if (result.getItem() != null) {
            swordService.message("Initialising ATOM entry generator for an Item");
            dsatom = new ItemEntryGenerator(swordService);
        } else if (result.getBitstream() != null) {
            swordService.message("Initialising ATOM entry generator for a Bitstream");
            dsatom = new BitstreamEntryGenerator(swordService);
        }
        if (dsatom == null) {
            log.error("The deposit failed, see exceptions for explanation");
            throw new DSpaceSWORDException("Result of deposit did not yield an Item or a Bitstream");
        }
        SWORDEntry entry = dsatom.getSWORDEntry(result, deposit);
        if (deposit.isNoOp()) {
            dep.undoDeposit(result);
            swordService.message("NoOp Requested: Removed all traces of submission");
        }
        entry.setNoOp(deposit.isNoOp());
        Date finish = new Date();
        long delta = finish.getTime() - start.getTime();
        swordService.message("Total time for deposit processing: " + delta + " ms");
        entry.setVerboseDescription(swordService.getVerboseDescription().toString());
        response.setEntry(entry);
        return response;
    }
}
