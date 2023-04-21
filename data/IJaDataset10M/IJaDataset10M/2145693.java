package edu.tufts.osidimpl.repository.nytimes;

public class RepositoryManager implements org.osid.repository.RepositoryManager {

    private org.osid.OsidContext _context = null;

    private java.util.Properties _configuration = null;

    private java.util.Vector _repositoryVector = new java.util.Vector();

    public org.osid.OsidContext getOsidContext() throws org.osid.repository.RepositoryException {
        return _context;
    }

    public void assignOsidContext(org.osid.OsidContext context) throws org.osid.repository.RepositoryException {
        _context = context;
    }

    /**
		Hand-off properties to the Configuration class for processing
		*/
    public void assignConfiguration(java.util.Properties configuration) throws org.osid.repository.RepositoryException {
        try {
            _configuration = configuration;
            Configuration conf = Configuration.getInstance();
            conf.setConfiguration(configuration);
            _repositoryVector = conf.getRepositories();
        } catch (Throwable t) {
            Utilities.log(t);
            throw new org.osid.repository.RepositoryException(org.osid.OsidException.CONFIGURATION_ERROR);
        }
    }

    public org.osid.repository.Repository createRepository(String displayName, String description, org.osid.shared.Type repositoryType) throws org.osid.repository.RepositoryException {
        throw new org.osid.repository.RepositoryException(org.osid.OsidException.UNIMPLEMENTED);
    }

    public void deleteRepository(org.osid.shared.Id repositoryId) throws org.osid.repository.RepositoryException {
        if (repositoryId == null) {
            throw new org.osid.repository.RepositoryException(org.osid.shared.SharedException.NULL_ARGUMENT);
        }
        throw new org.osid.repository.RepositoryException(org.osid.OsidException.UNIMPLEMENTED);
    }

    public org.osid.repository.RepositoryIterator getRepositories() throws org.osid.repository.RepositoryException {
        return new RepositoryIterator(_repositoryVector);
    }

    public org.osid.repository.RepositoryIterator getRepositoriesByType(org.osid.shared.Type repositoryType) throws org.osid.repository.RepositoryException {
        if (repositoryType == null) {
            throw new org.osid.repository.RepositoryException(org.osid.shared.SharedException.NULL_ARGUMENT);
        }
        java.util.Vector result = new java.util.Vector();
        org.osid.repository.RepositoryIterator repositoryIterator = getRepositories();
        while (repositoryIterator.hasNextRepository()) {
            org.osid.repository.Repository nextRepository = repositoryIterator.nextRepository();
            if (nextRepository.getType().isEqual(repositoryType)) {
                result.addElement(nextRepository);
            }
        }
        return new RepositoryIterator(result);
    }

    public org.osid.repository.Repository getRepository(org.osid.shared.Id repositoryId) throws org.osid.repository.RepositoryException {
        if (repositoryId == null) {
            throw new org.osid.repository.RepositoryException(org.osid.shared.SharedException.NULL_ARGUMENT);
        }
        try {
            org.osid.repository.RepositoryIterator repositoryIterator = getRepositories();
            while (repositoryIterator.hasNextRepository()) {
                org.osid.repository.Repository nextRepository = repositoryIterator.nextRepository();
                if (nextRepository.getId().isEqual(repositoryId)) {
                    return nextRepository;
                }
            }
            throw new org.osid.repository.RepositoryException(org.osid.repository.RepositoryException.UNKNOWN_ID);
        } catch (Throwable t) {
            Utilities.log(t.getMessage());
            throw new org.osid.repository.RepositoryException(org.osid.repository.RepositoryException.OPERATION_FAILED);
        }
    }

    public org.osid.repository.Asset getAsset(org.osid.shared.Id assetId) throws org.osid.repository.RepositoryException {
        if (assetId == null) {
            throw new org.osid.repository.RepositoryException(org.osid.shared.SharedException.NULL_ARGUMENT);
        }
        try {
            org.osid.repository.RepositoryIterator repositoryIterator = getRepositories();
            while (repositoryIterator.hasNextRepository()) {
                org.osid.repository.Repository nextRepository = repositoryIterator.nextRepository();
                try {
                    org.osid.repository.Asset asset = nextRepository.getAsset(assetId);
                    return asset;
                } catch (Throwable t) {
                }
            }
        } catch (Throwable t) {
            Utilities.log(t.getMessage());
            throw new org.osid.repository.RepositoryException(org.osid.repository.RepositoryException.OPERATION_FAILED);
        }
        throw new org.osid.repository.RepositoryException(org.osid.repository.RepositoryException.UNKNOWN_ID);
    }

    public org.osid.repository.Asset getAssetByDate(org.osid.shared.Id assetId, long date) throws org.osid.repository.RepositoryException {
        if (assetId == null) {
            throw new org.osid.repository.RepositoryException(org.osid.shared.SharedException.NULL_ARGUMENT);
        }
        try {
            org.osid.repository.RepositoryIterator repositoryIterator = getRepositories();
            while (repositoryIterator.hasNextRepository()) {
                org.osid.repository.Repository nextRepository = repositoryIterator.nextRepository();
                try {
                    org.osid.repository.Asset asset = nextRepository.getAssetByDate(assetId, date);
                    return asset;
                } catch (Throwable t) {
                }
            }
        } catch (Throwable t) {
            Utilities.log(t.getMessage());
            throw new org.osid.repository.RepositoryException(org.osid.repository.RepositoryException.OPERATION_FAILED);
        }
        throw new org.osid.repository.RepositoryException(org.osid.repository.RepositoryException.UNKNOWN_ID);
    }

    public org.osid.shared.LongValueIterator getAssetDates(org.osid.shared.Id assetId) throws org.osid.repository.RepositoryException {
        if (assetId == null) {
            throw new org.osid.repository.RepositoryException(org.osid.shared.SharedException.NULL_ARGUMENT);
        }
        java.util.Vector result = new java.util.Vector();
        try {
            org.osid.repository.RepositoryIterator repositoryIterator = getRepositories();
            while (repositoryIterator.hasNextRepository()) {
                org.osid.repository.Repository nextRepository = repositoryIterator.nextRepository();
                org.osid.shared.LongValueIterator longValueIterator = nextRepository.getAssetDates(assetId);
                while (longValueIterator.hasNextLongValue()) {
                    result.addElement(new Long(longValueIterator.nextLongValue()));
                }
            }
            return new LongValueIterator(result);
        } catch (Throwable t) {
            Utilities.log(t.getMessage());
            throw new org.osid.repository.RepositoryException(org.osid.repository.RepositoryException.OPERATION_FAILED);
        }
    }

    public org.osid.repository.AssetIterator getAssetsBySearch(org.osid.repository.Repository[] repositories, java.io.Serializable searchCriteria, org.osid.shared.Type searchType, org.osid.shared.Properties searchProperties) throws org.osid.repository.RepositoryException {
        if (repositories == null) {
            throw new org.osid.repository.RepositoryException(org.osid.shared.SharedException.NULL_ARGUMENT);
        }
        try {
            java.util.Vector results = new java.util.Vector();
            for (int j = 0; j < repositories.length; j++) {
                org.osid.repository.Repository nextRepository = repositories[j];
                try {
                    org.osid.repository.AssetIterator assetIterator = nextRepository.getAssetsBySearch(searchCriteria, searchType, searchProperties);
                    while (assetIterator.hasNextAsset()) {
                        results.addElement(assetIterator.nextAsset());
                    }
                } catch (Throwable t) {
                    Utilities.log(t.getMessage());
                }
            }
            return new AssetIterator(results);
        } catch (Throwable t) {
            Utilities.log(t.getMessage());
            throw new org.osid.repository.RepositoryException(org.osid.repository.RepositoryException.OPERATION_FAILED);
        }
    }

    public org.osid.shared.Id copyAsset(org.osid.repository.Repository repository, org.osid.shared.Id assetId) throws org.osid.repository.RepositoryException {
        if ((repository == null) || (assetId == null)) {
            throw new org.osid.repository.RepositoryException(org.osid.shared.SharedException.NULL_ARGUMENT);
        }
        throw new org.osid.repository.RepositoryException(org.osid.OsidException.UNIMPLEMENTED);
    }

    public org.osid.shared.TypeIterator getRepositoryTypes() throws org.osid.repository.RepositoryException {
        return Configuration.getInstance().getRepositoryTypes();
    }

    public void osidVersion_2_0() throws org.osid.repository.RepositoryException {
    }
}
