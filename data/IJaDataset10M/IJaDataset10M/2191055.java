package br.edu.ufcg.ccc.javalog.model;

import br.edu.ufcg.ccc.javalog.exceptions.PackageException;

/**
 * A OneDay Package
 * 
 * @author Allyson Lima, Diego Pedro, Victor Freire
 * @version 03/12/09
 */
public class PackageOneDay extends Package {

    /**
	 * Static final attribute for type package
	 */
    private static final String type = "24h";

    /**
	 * Constructor default
	 * 
	 * @param senderAddress
	 *            Address of the sender
	 * @param recipientAddress
	 *            Address of the recipient
	 * @param sourceFranchise
	 *            Source Franchise
	 * @param destinationFranchise
	 *            Destination Franchise
	 * @throws PackageException
	 *             Invalid arguments
	 */
    public PackageOneDay(Address senderAddress, Address recipientAddress, Franchise sourceFranchise, Franchise destinationFranchise) throws PackageException {
        super(senderAddress, recipientAddress, sourceFranchise, destinationFranchise);
    }

    /**
	 * Returns the type of the package
	 * 
	 * @return the type of the package
	 */
    @Override
    public String getType() {
        return type;
    }

    /**
	 * Moves this package to the next franchise following the normal mode
	 */
    @Override
    public void move() throws PackageException {
        if (getCurrentFranchise() == getDestinationFranchise()) throw new PackageException("move() should not be called! the package is already at the destination");
        if (getDestinationFranchise().hasAirport()) {
            setCurrentFranchise(getDestinationFranchise());
        } else {
            Franchise nearestAirportFranchise = getDestinationFranchise().getNearestAirport();
            if (nearestAirportFranchise == getCurrentFranchise()) {
                Franchise nextFranchise = getSourceFranchise().getNextFranchiseInShortestPath(getDestinationFranchise());
                setCurrentFranchise(nextFranchise);
            }
        }
    }
}
