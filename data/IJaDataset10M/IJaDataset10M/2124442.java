package fr.fg.client.data;

import java.util.HashMap;

public class IndexedAreaData {

    private AreaData areaData;

    private HashMap<Integer, FleetData> fleetsMap;

    private HashMap<Integer, SpaceStationData> spaceStationsMap;

    private HashMap<Integer, StarSystemData> systemsMap;

    private HashMap<Integer, DoodadData> doodadsMap;

    private HashMap<Integer, MarkerData> markersMap;

    private HashMap<Integer, WardData> wardsMap;

    private HashMap<Integer, ContractMarkerData> contractMarkersMap;

    private HashMap<Integer, BlackHoleData> blackHolesMap;

    private HashMap<Integer, HyperspaceSignatureData> hyperspaceSignaturesMap;

    private HashMap<Integer, AsteroidsData> asteroidsMap;

    private HashMap<Long, StructureData> structuresMap;

    private HashMap<Integer, GravityWellData> gravityWellsMap;

    public IndexedAreaData(AreaData areaData) {
        this.areaData = areaData;
        this.fleetsMap = new HashMap<Integer, FleetData>();
        for (int i = 0; i < areaData.getFleetsCount(); i++) fleetsMap.put(areaData.getFleetAt(i).getId(), areaData.getFleetAt(i));
        this.spaceStationsMap = new HashMap<Integer, SpaceStationData>();
        for (int i = 0; i < areaData.getSpaceStationsCount(); i++) spaceStationsMap.put(areaData.getSpaceStationAt(i).getId(), areaData.getSpaceStationAt(i));
        this.systemsMap = new HashMap<Integer, StarSystemData>();
        for (int i = 0; i < areaData.getSystemsCount(); i++) systemsMap.put(areaData.getSystemAt(i).getId(), areaData.getSystemAt(i));
        this.doodadsMap = new HashMap<Integer, DoodadData>();
        for (int i = 0; i < areaData.getDoodadsCount(); i++) doodadsMap.put(areaData.getDoodadAt(i).getId(), areaData.getDoodadAt(i));
        this.markersMap = new HashMap<Integer, MarkerData>();
        for (int i = 0; i < areaData.getMarkersCount(); i++) markersMap.put(areaData.getMarkerAt(i).getId(), areaData.getMarkerAt(i));
        this.wardsMap = new HashMap<Integer, WardData>();
        for (int i = 0; i < areaData.getWardsCount(); i++) wardsMap.put(areaData.getWardAt(i).getId(), areaData.getWardAt(i));
        this.contractMarkersMap = new HashMap<Integer, ContractMarkerData>();
        for (int i = 0; i < areaData.getContractMarkersCount(); i++) contractMarkersMap.put(areaData.getContractMarkerAt(i).getId(), areaData.getContractMarkerAt(i));
        this.blackHolesMap = new HashMap<Integer, BlackHoleData>();
        for (int i = 0; i < areaData.getBlackHolesCount(); i++) blackHolesMap.put(areaData.getBlackHoleAt(i).getId(), areaData.getBlackHoleAt(i));
        this.hyperspaceSignaturesMap = new HashMap<Integer, HyperspaceSignatureData>();
        for (int i = 0; i < areaData.getHyperspaceSignaturesCount(); i++) hyperspaceSignaturesMap.put(areaData.getHyperspaceSignatureAt(i).getId(), areaData.getHyperspaceSignatureAt(i));
        this.asteroidsMap = new HashMap<Integer, AsteroidsData>();
        for (int i = 0; i < areaData.getAsteroidsCount(); i++) asteroidsMap.put(areaData.getAsteroidsAt(i).getId(), areaData.getAsteroidsAt(i));
        this.structuresMap = new HashMap<Long, StructureData>();
        for (int i = 0; i < areaData.getStructuresCount(); i++) structuresMap.put((long) areaData.getStructureAt(i).getId(), areaData.getStructureAt(i));
        this.gravityWellsMap = new HashMap<Integer, GravityWellData>();
        for (int i = 0; i < areaData.getGravityWellsCount(); i++) gravityWellsMap.put(areaData.getGravityWellAt(i).getId(), areaData.getGravityWellAt(i));
    }

    public int getId() {
        return areaData.getId();
    }

    public String getName() {
        return areaData.getName();
    }

    public PlayerSectorData getSector() {
        return areaData.getSector();
    }

    public int getWidth() {
        return areaData.getWidth();
    }

    public int getHeight() {
        return areaData.getHeight();
    }

    public int getX() {
        return areaData.getX();
    }

    public int getY() {
        return areaData.getY();
    }

    public int getNebula() {
        return areaData.getNebula();
    }

    public boolean hasDomination() {
        return areaData.hasDomination();
    }

    public String getDomination() {
        return areaData.getDomination();
    }

    public int getSystemsCount() {
        return areaData.getSystemsCount();
    }

    public StarSystemData getSystemAt(int index) {
        return areaData.getSystemAt(index);
    }

    public StarSystemData getSystemById(int id) {
        return systemsMap.get(id);
    }

    public int getAsteroidsCount() {
        return areaData.getAsteroidsCount();
    }

    public AsteroidsData getAsteroidsAt(int index) {
        return areaData.getAsteroidsAt(index);
    }

    public AsteroidsData getAsteroidsById(int id) {
        return asteroidsMap.get(id);
    }

    public int getDoodadsCount() {
        return areaData.getDoodadsCount();
    }

    public DoodadData getDoodadAt(int index) {
        return areaData.getDoodadAt(index);
    }

    public DoodadData getDoodadById(int id) {
        return doodadsMap.get(id);
    }

    public int getGatesCount() {
        return areaData.getGatesCount();
    }

    public GateData getGateAt(int index) {
        return areaData.getGateAt(index);
    }

    public int getBlackHolesCount() {
        return areaData.getBlackHolesCount();
    }

    public BlackHoleData getBlackHoleAt(int index) {
        return areaData.getBlackHoleAt(index);
    }

    public BlackHoleData getBlackHoleById(int id) {
        return blackHolesMap.get(id);
    }

    public int getMarkersCount() {
        return areaData.getMarkersCount();
    }

    public MarkerData getMarkerAt(int index) {
        return areaData.getMarkerAt(index);
    }

    public MarkerData getMarkerById(int id) {
        return markersMap.get(id);
    }

    public int getFleetsCount() {
        return areaData.getFleetsCount();
    }

    public FleetData getFleetAt(int index) {
        return areaData.getFleetAt(index);
    }

    public FleetData getFleetById(int id) {
        return fleetsMap.get(id);
    }

    public int getBanksCount() {
        return areaData.getBanksCount();
    }

    public BankData getBankAt(int index) {
        return areaData.getBankAt(index);
    }

    public int getTradeCentersCount() {
        return areaData.getTradeCentersCount();
    }

    public TradeCenterData getTradeCenterAt(int index) {
        return areaData.getTradeCenterAt(index);
    }

    public int getSpaceStationsCount() {
        return areaData.getSpaceStationsCount();
    }

    public SpaceStationData getSpaceStationAt(int index) {
        return areaData.getSpaceStationAt(index);
    }

    public SpaceStationData getSpaceStationById(int id) {
        return spaceStationsMap.get(id);
    }

    public int getHyperspaceSignaturesCount() {
        return areaData.getHyperspaceSignaturesCount();
    }

    public HyperspaceSignatureData getHyperspaceSignatureAt(int index) {
        return areaData.getHyperspaceSignatureAt(index);
    }

    public HyperspaceSignatureData getHyperspaceSignatureById(int id) {
        return hyperspaceSignaturesMap.get(id);
    }

    public int getContractMarkersCount() {
        return areaData.getContractMarkersCount();
    }

    public ContractMarkerData getContractMarkerAt(int index) {
        return areaData.getContractMarkerAt(index);
    }

    public ContractMarkerData getContractMarkerById(int id) {
        return contractMarkersMap.get(id);
    }

    public int getWardsCount() {
        return areaData.getWardsCount();
    }

    public WardData getWardAt(int index) {
        return areaData.getWardAt(index);
    }

    public WardData getWardById(int id) {
        return wardsMap.get(id);
    }

    public int getStructuresCount() {
        return areaData.getStructuresCount();
    }

    public StructureData getStructureAt(int index) {
        return areaData.getStructureAt(index);
    }

    public StructureData getStructureById(long id) {
        return structuresMap.get(id);
    }

    public int getGravityWellsCount() {
        return areaData.getGravityWellsCount();
    }

    public GravityWellData getGravityWellAt(int index) {
        return areaData.getGravityWellAt(index);
    }

    public GravityWellData getGravityWellById(int id) {
        return gravityWellsMap.get(id);
    }
}
