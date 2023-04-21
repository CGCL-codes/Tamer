package org.dasein.cloud.services.rdbms;

import java.util.Collection;
import java.util.Locale;
import org.dasein.cloud.CloudException;
import org.dasein.cloud.InternalException;
import org.dasein.cloud.TimeWindow;

public interface RelationalDatabaseServices {

    public void addAccess(String providerDatabaseId, String sourceCidr) throws CloudException, InternalException;

    public void alterDatabase(String providerDatabaseId, boolean applyImmediately, String productSize, int storageInGigabytes, String configurationId, String newAdminUser, String newAdminPassword, int newPort, int snapshotRetentionInDays, TimeWindow preferredMaintenanceWindow, TimeWindow preferredBackupWindow) throws CloudException, InternalException;

    public String createFromScratch(String dataSourceName, DatabaseProduct product, String withAdminUser, String withAdminPassword, int hostPort) throws CloudException, InternalException;

    public String createFromLatest(String dataSourceName, String providerDatabaseId, String productSize, String providerDataCenterId, int hostPort) throws InternalException, CloudException;

    public String createFromSnapshot(String dataSourceName, String providerDatabaseId, String providerDbSnapshotId, String productSize, String providerDataCenterId, int hostPort) throws CloudException, InternalException;

    public String createFromTimestamp(String dataSourceName, String providerDatabaseId, long beforeTimestamp, String productSize, String providerDataCenterId, int hostPort) throws InternalException, CloudException;

    public DatabaseConfiguration getConfiguration(String providerConfigurationId) throws CloudException, InternalException;

    public Database getDatabase(String providerDatabaseId) throws CloudException, InternalException;

    public Iterable<DatabaseEngine> getDatabaseEngines() throws CloudException, InternalException;

    public Iterable<DatabaseProduct> getDatabaseProducts(DatabaseEngine forEngine) throws CloudException, InternalException;

    public String getProviderTermForDatabase(Locale locale);

    public String getProviderTermForSnapshot(Locale locale);

    public DatabaseSnapshot getSnapshot(String providerDbSnapshotId) throws CloudException, InternalException;

    public boolean isSubscribed() throws CloudException, InternalException;

    public boolean isSupportsFirewallRules();

    public boolean isSupportsHighAvailability() throws CloudException, InterruptedException;

    public boolean isSupportsLowAvailability() throws CloudException, InterruptedException;

    public boolean isSupportsMaintenanceWindows();

    public boolean isSupportsSnapshots();

    public Iterable<String> listAccess(String toProviderDatabaseId) throws CloudException, InternalException;

    public Iterable<DatabaseConfiguration> listConfigurations() throws CloudException, InternalException;

    public Iterable<Database> listDatabases() throws CloudException, InternalException;

    public Collection<ConfigurationParameter> listParameters(String forProviderConfigurationId) throws CloudException, InternalException;

    public Iterable<DatabaseSnapshot> listSnapshots(String forOptionalProviderDatabaseId) throws CloudException, InternalException;

    public void removeConfiguration(String providerConfigurationId) throws CloudException, InternalException;

    public void removeDatabase(String providerDatabaseId) throws CloudException, InternalException;

    public void removeSnapshot(String providerSnapshotId) throws CloudException, InternalException;

    public void resetConfiguration(String providerConfigurationId, String... parameters) throws CloudException, InternalException;

    public void restart(String providerDatabaseId, boolean blockUntilDone) throws CloudException, InternalException;

    public void revokeAccess(String providerDatabaseId, String sourceCide) throws CloudException, InternalException;

    public void updateConfiguration(String providerConfigurationId, ConfigurationParameter... parameters) throws CloudException, InternalException;

    public DatabaseSnapshot snapshot(String providerDatabaseId, String name) throws CloudException, InternalException;
}
