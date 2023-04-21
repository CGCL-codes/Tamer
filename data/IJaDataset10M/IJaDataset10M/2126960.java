package iec61970.generation.production;

import iec61970.core.PowerSystemResource;
import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Reservoir</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * A water storage facility within a hydro system, including: ponds, lakes, lagoons, and rivers. The storage is usually behind some type of dam.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link iec61970.generation.production.Reservoir#getActiveStorageCapacity <em>Active Storage Capacity</em>}</li>
 *   <li>{@link iec61970.generation.production.Reservoir#getEnergyStorageRating <em>Energy Storage Rating</em>}</li>
 *   <li>{@link iec61970.generation.production.Reservoir#getFullSupplyLevel <em>Full Supply Level</em>}</li>
 *   <li>{@link iec61970.generation.production.Reservoir#getGrossCapacity <em>Gross Capacity</em>}</li>
 *   <li>{@link iec61970.generation.production.Reservoir#getNormalMinOperateLevel <em>Normal Min Operate Level</em>}</li>
 *   <li>{@link iec61970.generation.production.Reservoir#getRiverOutletWorks <em>River Outlet Works</em>}</li>
 *   <li>{@link iec61970.generation.production.Reservoir#getSpillTravelDelay <em>Spill Travel Delay</em>}</li>
 *   <li>{@link iec61970.generation.production.Reservoir#getSpillwayCapacity <em>Spillway Capacity</em>}</li>
 *   <li>{@link iec61970.generation.production.Reservoir#getSpillwayCrestLength <em>Spillway Crest Length</em>}</li>
 *   <li>{@link iec61970.generation.production.Reservoir#getSpillwayCrestLevel <em>Spillway Crest Level</em>}</li>
 *   <li>{@link iec61970.generation.production.Reservoir#getSpillWayGateType <em>Spill Way Gate Type</em>}</li>
 *   <li>{@link iec61970.generation.production.Reservoir#getHydroPowerPlants <em>Hydro Power Plants</em>}</li>
 *   <li>{@link iec61970.generation.production.Reservoir#getUpstreamFrom <em>Upstream From</em>}</li>
 *   <li>{@link iec61970.generation.production.Reservoir#getLevelVsVolumeCurve <em>Level Vs Volume Curve</em>}</li>
 *   <li>{@link iec61970.generation.production.Reservoir#getTargetLevelSchedule <em>Target Level Schedule</em>}</li>
 *   <li>{@link iec61970.generation.production.Reservoir#getInflowForecast <em>Inflow Forecast</em>}</li>
 *   <li>{@link iec61970.generation.production.Reservoir#getSpillsInto <em>Spills Into</em>}</li>
 *   <li>{@link iec61970.generation.production.Reservoir#getSpillsFrom <em>Spills From</em>}</li>
 * </ul>
 * </p>
 *
 * @see iec61970.generation.production.ProductionPackage#getReservoir()
 * @model
 * @generated
 */
public interface Reservoir extends PowerSystemResource {

    /**
	 * Returns the value of the '<em><b>Active Storage Capacity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Storage volume (in Mm3) between the full supply level and the normal minimum operating level
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Active Storage Capacity</em>' attribute.
	 * @see #setActiveStorageCapacity(String)
	 * @see iec61970.generation.production.ProductionPackage#getReservoir_ActiveStorageCapacity()
	 * @model dataType="iec61970.domain.Volume"
	 * @generated
	 */
    String getActiveStorageCapacity();

    /**
	 * Sets the value of the '{@link iec61970.generation.production.Reservoir#getActiveStorageCapacity <em>Active Storage Capacity</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Active Storage Capacity</em>' attribute.
	 * @see #getActiveStorageCapacity()
	 * @generated
	 */
    void setActiveStorageCapacity(String value);

    /**
	 * Returns the value of the '<em><b>Energy Storage Rating</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The reservoir's energy storage rating in energy for given head conditions
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Energy Storage Rating</em>' attribute.
	 * @see #setEnergyStorageRating(String)
	 * @see iec61970.generation.production.ProductionPackage#getReservoir_EnergyStorageRating()
	 * @model dataType="iec61970.domain.Float"
	 * @generated
	 */
    String getEnergyStorageRating();

    /**
	 * Sets the value of the '{@link iec61970.generation.production.Reservoir#getEnergyStorageRating <em>Energy Storage Rating</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Energy Storage Rating</em>' attribute.
	 * @see #getEnergyStorageRating()
	 * @generated
	 */
    void setEnergyStorageRating(String value);

    /**
	 * Returns the value of the '<em><b>Full Supply Level</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Full supply level, above which water will spill. This can be the spillway crest level or the top of closed gates.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Full Supply Level</em>' attribute.
	 * @see #setFullSupplyLevel(String)
	 * @see iec61970.generation.production.ProductionPackage#getReservoir_FullSupplyLevel()
	 * @model dataType="iec61970.domain.WaterLevel"
	 * @generated
	 */
    String getFullSupplyLevel();

    /**
	 * Sets the value of the '{@link iec61970.generation.production.Reservoir#getFullSupplyLevel <em>Full Supply Level</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Full Supply Level</em>' attribute.
	 * @see #getFullSupplyLevel()
	 * @generated
	 */
    void setFullSupplyLevel(String value);

    /**
	 * Returns the value of the '<em><b>Gross Capacity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Total capacity of reservoir
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Gross Capacity</em>' attribute.
	 * @see #setGrossCapacity(String)
	 * @see iec61970.generation.production.ProductionPackage#getReservoir_GrossCapacity()
	 * @model dataType="iec61970.domain.Volume"
	 * @generated
	 */
    String getGrossCapacity();

    /**
	 * Sets the value of the '{@link iec61970.generation.production.Reservoir#getGrossCapacity <em>Gross Capacity</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Gross Capacity</em>' attribute.
	 * @see #getGrossCapacity()
	 * @generated
	 */
    void setGrossCapacity(String value);

    /**
	 * Returns the value of the '<em><b>Normal Min Operate Level</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Normal minimum operating level below which the penstocks will draw air
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Normal Min Operate Level</em>' attribute.
	 * @see #setNormalMinOperateLevel(String)
	 * @see iec61970.generation.production.ProductionPackage#getReservoir_NormalMinOperateLevel()
	 * @model dataType="iec61970.domain.WaterLevel"
	 * @generated
	 */
    String getNormalMinOperateLevel();

    /**
	 * Sets the value of the '{@link iec61970.generation.production.Reservoir#getNormalMinOperateLevel <em>Normal Min Operate Level</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Normal Min Operate Level</em>' attribute.
	 * @see #getNormalMinOperateLevel()
	 * @generated
	 */
    void setNormalMinOperateLevel(String value);

    /**
	 * Returns the value of the '<em><b>River Outlet Works</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * River outlet works for riparian right releases or other purposes
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>River Outlet Works</em>' attribute.
	 * @see #setRiverOutletWorks(String)
	 * @see iec61970.generation.production.ProductionPackage#getReservoir_RiverOutletWorks()
	 * @model dataType="iec61970.domain.String"
	 * @generated
	 */
    String getRiverOutletWorks();

    /**
	 * Sets the value of the '{@link iec61970.generation.production.Reservoir#getRiverOutletWorks <em>River Outlet Works</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>River Outlet Works</em>' attribute.
	 * @see #getRiverOutletWorks()
	 * @generated
	 */
    void setRiverOutletWorks(String value);

    /**
	 * Returns the value of the '<em><b>Spill Travel Delay</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The spillway water travel delay to the next downstream reservoir
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Spill Travel Delay</em>' attribute.
	 * @see #setSpillTravelDelay(String)
	 * @see iec61970.generation.production.ProductionPackage#getReservoir_SpillTravelDelay()
	 * @model dataType="iec61970.domain.Seconds"
	 * @generated
	 */
    String getSpillTravelDelay();

    /**
	 * Sets the value of the '{@link iec61970.generation.production.Reservoir#getSpillTravelDelay <em>Spill Travel Delay</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Spill Travel Delay</em>' attribute.
	 * @see #getSpillTravelDelay()
	 * @generated
	 */
    void setSpillTravelDelay(String value);

    /**
	 * Returns the value of the '<em><b>Spillway Capacity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The flow capacity of the spillway in cubic meters per second
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Spillway Capacity</em>' attribute.
	 * @see #setSpillwayCapacity(String)
	 * @see iec61970.generation.production.ProductionPackage#getReservoir_SpillwayCapacity()
	 * @model dataType="iec61970.domain.Float"
	 * @generated
	 */
    String getSpillwayCapacity();

    /**
	 * Sets the value of the '{@link iec61970.generation.production.Reservoir#getSpillwayCapacity <em>Spillway Capacity</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Spillway Capacity</em>' attribute.
	 * @see #getSpillwayCapacity()
	 * @generated
	 */
    void setSpillwayCapacity(String value);

    /**
	 * Returns the value of the '<em><b>Spillway Crest Length</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The length of the spillway crest in meters
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Spillway Crest Length</em>' attribute.
	 * @see #setSpillwayCrestLength(String)
	 * @see iec61970.generation.production.ProductionPackage#getReservoir_SpillwayCrestLength()
	 * @model dataType="iec61970.domain.Float"
	 * @generated
	 */
    String getSpillwayCrestLength();

    /**
	 * Sets the value of the '{@link iec61970.generation.production.Reservoir#getSpillwayCrestLength <em>Spillway Crest Length</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Spillway Crest Length</em>' attribute.
	 * @see #getSpillwayCrestLength()
	 * @generated
	 */
    void setSpillwayCrestLength(String value);

    /**
	 * Returns the value of the '<em><b>Spillway Crest Level</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Spillway crest level above which water will spill
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Spillway Crest Level</em>' attribute.
	 * @see #setSpillwayCrestLevel(String)
	 * @see iec61970.generation.production.ProductionPackage#getReservoir_SpillwayCrestLevel()
	 * @model dataType="iec61970.domain.WaterLevel"
	 * @generated
	 */
    String getSpillwayCrestLevel();

    /**
	 * Sets the value of the '{@link iec61970.generation.production.Reservoir#getSpillwayCrestLevel <em>Spillway Crest Level</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Spillway Crest Level</em>' attribute.
	 * @see #getSpillwayCrestLevel()
	 * @generated
	 */
    void setSpillwayCrestLevel(String value);

    /**
	 * Returns the value of the '<em><b>Spill Way Gate Type</b></em>' attribute.
	 * The literals are from the enumeration {@link iec61970.generation.production.SpillwayGateType}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Type of spillway gate, including parameters
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Spill Way Gate Type</em>' attribute.
	 * @see iec61970.generation.production.SpillwayGateType
	 * @see #setSpillWayGateType(SpillwayGateType)
	 * @see iec61970.generation.production.ProductionPackage#getReservoir_SpillWayGateType()
	 * @model
	 * @generated
	 */
    SpillwayGateType getSpillWayGateType();

    /**
	 * Sets the value of the '{@link iec61970.generation.production.Reservoir#getSpillWayGateType <em>Spill Way Gate Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Spill Way Gate Type</em>' attribute.
	 * @see iec61970.generation.production.SpillwayGateType
	 * @see #getSpillWayGateType()
	 * @generated
	 */
    void setSpillWayGateType(SpillwayGateType value);

    /**
	 * Returns the value of the '<em><b>Hydro Power Plants</b></em>' reference list.
	 * The list contents are of type {@link iec61970.generation.production.HydroPowerPlant}.
	 * It is bidirectional and its opposite is '{@link iec61970.generation.production.HydroPowerPlant#getReservoir <em>Reservoir</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Generators discharge water to or pumps are supplied water from a downstream reservoir
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Hydro Power Plants</em>' reference list.
	 * @see iec61970.generation.production.ProductionPackage#getReservoir_HydroPowerPlants()
	 * @see iec61970.generation.production.HydroPowerPlant#getReservoir
	 * @model opposite="Reservoir"
	 * @generated
	 */
    EList<HydroPowerPlant> getHydroPowerPlants();

    /**
	 * Returns the value of the '<em><b>Upstream From</b></em>' reference list.
	 * The list contents are of type {@link iec61970.generation.production.HydroPowerPlant}.
	 * It is bidirectional and its opposite is '{@link iec61970.generation.production.HydroPowerPlant#getGenSourcePumpDischarge <em>Gen Source Pump Discharge</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Generators are supplied water from or pumps discharge water to an upstream reservoir
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Upstream From</em>' reference list.
	 * @see iec61970.generation.production.ProductionPackage#getReservoir_UpstreamFrom()
	 * @see iec61970.generation.production.HydroPowerPlant#getGenSourcePumpDischarge
	 * @model opposite="GenSourcePumpDischarge"
	 * @generated
	 */
    EList<HydroPowerPlant> getUpstreamFrom();

    /**
	 * Returns the value of the '<em><b>Level Vs Volume Curve</b></em>' reference list.
	 * The list contents are of type {@link iec61970.generation.production.LevelVsVolumeCurve}.
	 * It is bidirectional and its opposite is '{@link iec61970.generation.production.LevelVsVolumeCurve#getReservoir <em>Reservoir</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * A reservoir may have a level versus volume relationship.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Level Vs Volume Curve</em>' reference list.
	 * @see iec61970.generation.production.ProductionPackage#getReservoir_LevelVsVolumeCurve()
	 * @see iec61970.generation.production.LevelVsVolumeCurve#getReservoir
	 * @model opposite="Reservoir"
	 * @generated
	 */
    EList<LevelVsVolumeCurve> getLevelVsVolumeCurve();

    /**
	 * Returns the value of the '<em><b>Target Level Schedule</b></em>' reference.
	 * It is bidirectional and its opposite is '{@link iec61970.generation.production.TargetLevelSchedule#getReservoir <em>Reservoir</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * A reservoir may have a water level target schedule.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Target Level Schedule</em>' reference.
	 * @see #setTargetLevelSchedule(TargetLevelSchedule)
	 * @see iec61970.generation.production.ProductionPackage#getReservoir_TargetLevelSchedule()
	 * @see iec61970.generation.production.TargetLevelSchedule#getReservoir
	 * @model opposite="Reservoir"
	 * @generated
	 */
    TargetLevelSchedule getTargetLevelSchedule();

    /**
	 * Sets the value of the '{@link iec61970.generation.production.Reservoir#getTargetLevelSchedule <em>Target Level Schedule</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Target Level Schedule</em>' reference.
	 * @see #getTargetLevelSchedule()
	 * @generated
	 */
    void setTargetLevelSchedule(TargetLevelSchedule value);

    /**
	 * Returns the value of the '<em><b>Inflow Forecast</b></em>' reference list.
	 * The list contents are of type {@link iec61970.generation.production.InflowForecast}.
	 * It is bidirectional and its opposite is '{@link iec61970.generation.production.InflowForecast#getReservoir <em>Reservoir</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * A reservoir may have a "natural" inflow forecast.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Inflow Forecast</em>' reference list.
	 * @see iec61970.generation.production.ProductionPackage#getReservoir_InflowForecast()
	 * @see iec61970.generation.production.InflowForecast#getReservoir
	 * @model opposite="Reservoir"
	 * @generated
	 */
    EList<InflowForecast> getInflowForecast();

    /**
	 * Returns the value of the '<em><b>Spills Into</b></em>' reference list.
	 * The list contents are of type {@link iec61970.generation.production.Reservoir}.
	 * It is bidirectional and its opposite is '{@link iec61970.generation.production.Reservoir#getSpillsFrom <em>Spills From</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * A reservoir may spill into a downstream reservoir
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Spills Into</em>' reference list.
	 * @see iec61970.generation.production.ProductionPackage#getReservoir_SpillsInto()
	 * @see iec61970.generation.production.Reservoir#getSpillsFrom
	 * @model opposite="SpillsFrom"
	 * @generated
	 */
    EList<Reservoir> getSpillsInto();

    /**
	 * Returns the value of the '<em><b>Spills From</b></em>' reference.
	 * It is bidirectional and its opposite is '{@link iec61970.generation.production.Reservoir#getSpillsInto <em>Spills Into</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * A reservoir may spill into a downstream reservoir
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Spills From</em>' reference.
	 * @see #setSpillsFrom(Reservoir)
	 * @see iec61970.generation.production.ProductionPackage#getReservoir_SpillsFrom()
	 * @see iec61970.generation.production.Reservoir#getSpillsInto
	 * @model opposite="SpillsInto"
	 * @generated
	 */
    Reservoir getSpillsFrom();

    /**
	 * Sets the value of the '{@link iec61970.generation.production.Reservoir#getSpillsFrom <em>Spills From</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Spills From</em>' reference.
	 * @see #getSpillsFrom()
	 * @generated
	 */
    void setSpillsFrom(Reservoir value);
}
