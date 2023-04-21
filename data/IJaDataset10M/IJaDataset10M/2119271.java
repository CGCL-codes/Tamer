package iec61970.generation.production;

import iec61970.core.IdentifiedObject;
import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Fossil Fuel</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * The fossil fuel consumed by the non-nuclear thermal generating units, e.g., coal, oil, gas
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link iec61970.generation.production.FossilFuel#getFossilFuelType <em>Fossil Fuel Type</em>}</li>
 *   <li>{@link iec61970.generation.production.FossilFuel#getFuelCost <em>Fuel Cost</em>}</li>
 *   <li>{@link iec61970.generation.production.FossilFuel#getFuelDispatchCost <em>Fuel Dispatch Cost</em>}</li>
 *   <li>{@link iec61970.generation.production.FossilFuel#getFuelEffFactor <em>Fuel Eff Factor</em>}</li>
 *   <li>{@link iec61970.generation.production.FossilFuel#getFuelHandlingCost <em>Fuel Handling Cost</em>}</li>
 *   <li>{@link iec61970.generation.production.FossilFuel#getFuelHeatContent <em>Fuel Heat Content</em>}</li>
 *   <li>{@link iec61970.generation.production.FossilFuel#getFuelMixture <em>Fuel Mixture</em>}</li>
 *   <li>{@link iec61970.generation.production.FossilFuel#getFuelSulfur <em>Fuel Sulfur</em>}</li>
 *   <li>{@link iec61970.generation.production.FossilFuel#getHighBreakpointP <em>High Breakpoint P</em>}</li>
 *   <li>{@link iec61970.generation.production.FossilFuel#getLowBreakpointP <em>Low Breakpoint P</em>}</li>
 *   <li>{@link iec61970.generation.production.FossilFuel#getFuelAllocationSchedule <em>Fuel Allocation Schedule</em>}</li>
 *   <li>{@link iec61970.generation.production.FossilFuel#getThermalGeneratingUnit <em>Thermal Generating Unit</em>}</li>
 * </ul>
 * </p>
 *
 * @see iec61970.generation.production.ProductionPackage#getFossilFuel()
 * @model
 * @generated
 */
public interface FossilFuel extends IdentifiedObject {

    /**
	 * Returns the value of the '<em><b>Fossil Fuel Type</b></em>' attribute.
	 * The literals are from the enumeration {@link iec61970.generation.production.FuelType}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The type of fossil fuel, such as coal, oil, or gas.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Fossil Fuel Type</em>' attribute.
	 * @see iec61970.generation.production.FuelType
	 * @see #setFossilFuelType(FuelType)
	 * @see iec61970.generation.production.ProductionPackage#getFossilFuel_FossilFuelType()
	 * @model
	 * @generated
	 */
    FuelType getFossilFuelType();

    /**
	 * Sets the value of the '{@link iec61970.generation.production.FossilFuel#getFossilFuelType <em>Fossil Fuel Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Fossil Fuel Type</em>' attribute.
	 * @see iec61970.generation.production.FuelType
	 * @see #getFossilFuelType()
	 * @generated
	 */
    void setFossilFuelType(FuelType value);

    /**
	 * Returns the value of the '<em><b>Fuel Cost</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The cost in terms of heat value for the given type of fuel
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Fuel Cost</em>' attribute.
	 * @see #setFuelCost(String)
	 * @see iec61970.generation.production.ProductionPackage#getFossilFuel_FuelCost()
	 * @model dataType="iec61970.generation.production.CostPerHeatUnit"
	 * @generated
	 */
    String getFuelCost();

    /**
	 * Sets the value of the '{@link iec61970.generation.production.FossilFuel#getFuelCost <em>Fuel Cost</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Fuel Cost</em>' attribute.
	 * @see #getFuelCost()
	 * @generated
	 */
    void setFuelCost(String value);

    /**
	 * Returns the value of the '<em><b>Fuel Dispatch Cost</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The cost of fuel used for economic dispatching which includes: fuel cost, transportation cost,  and incremental maintenance cost
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Fuel Dispatch Cost</em>' attribute.
	 * @see #setFuelDispatchCost(String)
	 * @see iec61970.generation.production.ProductionPackage#getFossilFuel_FuelDispatchCost()
	 * @model dataType="iec61970.generation.production.CostPerHeatUnit"
	 * @generated
	 */
    String getFuelDispatchCost();

    /**
	 * Sets the value of the '{@link iec61970.generation.production.FossilFuel#getFuelDispatchCost <em>Fuel Dispatch Cost</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Fuel Dispatch Cost</em>' attribute.
	 * @see #getFuelDispatchCost()
	 * @generated
	 */
    void setFuelDispatchCost(String value);

    /**
	 * Returns the value of the '<em><b>Fuel Eff Factor</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The efficiency factor for the fuel (per unit) in terms of the effective energy absorbed
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Fuel Eff Factor</em>' attribute.
	 * @see #setFuelEffFactor(String)
	 * @see iec61970.generation.production.ProductionPackage#getFossilFuel_FuelEffFactor()
	 * @model dataType="iec61970.domain.PU"
	 * @generated
	 */
    String getFuelEffFactor();

    /**
	 * Sets the value of the '{@link iec61970.generation.production.FossilFuel#getFuelEffFactor <em>Fuel Eff Factor</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Fuel Eff Factor</em>' attribute.
	 * @see #getFuelEffFactor()
	 * @generated
	 */
    void setFuelEffFactor(String value);

    /**
	 * Returns the value of the '<em><b>Fuel Handling Cost</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Handling and processing cost associated with this fuel
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Fuel Handling Cost</em>' attribute.
	 * @see #setFuelHandlingCost(String)
	 * @see iec61970.generation.production.ProductionPackage#getFossilFuel_FuelHandlingCost()
	 * @model dataType="iec61970.generation.production.CostPerHeatUnit"
	 * @generated
	 */
    String getFuelHandlingCost();

    /**
	 * Sets the value of the '{@link iec61970.generation.production.FossilFuel#getFuelHandlingCost <em>Fuel Handling Cost</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Fuel Handling Cost</em>' attribute.
	 * @see #getFuelHandlingCost()
	 * @generated
	 */
    void setFuelHandlingCost(String value);

    /**
	 * Returns the value of the '<em><b>Fuel Heat Content</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The amount of heat per weight (or volume) of the given type of fuel
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Fuel Heat Content</em>' attribute.
	 * @see #setFuelHeatContent(String)
	 * @see iec61970.generation.production.ProductionPackage#getFossilFuel_FuelHeatContent()
	 * @model dataType="iec61970.domain.Float"
	 * @generated
	 */
    String getFuelHeatContent();

    /**
	 * Sets the value of the '{@link iec61970.generation.production.FossilFuel#getFuelHeatContent <em>Fuel Heat Content</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Fuel Heat Content</em>' attribute.
	 * @see #getFuelHeatContent()
	 * @generated
	 */
    void setFuelHeatContent(String value);

    /**
	 * Returns the value of the '<em><b>Fuel Mixture</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The amount in percent of the given type of fuel , when multiple fuels are being consumed
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Fuel Mixture</em>' attribute.
	 * @see #setFuelMixture(String)
	 * @see iec61970.generation.production.ProductionPackage#getFossilFuel_FuelMixture()
	 * @model dataType="iec61970.domain.PerCent"
	 * @generated
	 */
    String getFuelMixture();

    /**
	 * Sets the value of the '{@link iec61970.generation.production.FossilFuel#getFuelMixture <em>Fuel Mixture</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Fuel Mixture</em>' attribute.
	 * @see #getFuelMixture()
	 * @generated
	 */
    void setFuelMixture(String value);

    /**
	 * Returns the value of the '<em><b>Fuel Sulfur</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The fuel's fraction of pollution credit per unit of heat content
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Fuel Sulfur</em>' attribute.
	 * @see #setFuelSulfur(String)
	 * @see iec61970.generation.production.ProductionPackage#getFossilFuel_FuelSulfur()
	 * @model dataType="iec61970.domain.PU"
	 * @generated
	 */
    String getFuelSulfur();

    /**
	 * Sets the value of the '{@link iec61970.generation.production.FossilFuel#getFuelSulfur <em>Fuel Sulfur</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Fuel Sulfur</em>' attribute.
	 * @see #getFuelSulfur()
	 * @generated
	 */
    void setFuelSulfur(String value);

    /**
	 * Returns the value of the '<em><b>High Breakpoint P</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The active power output level of the unit at which the given type of fuel is switched on. This fuel (e.g., oil) is sometimes used to supplement the base fuel (e.g., coal) at high active power output levels.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>High Breakpoint P</em>' attribute.
	 * @see #setHighBreakpointP(String)
	 * @see iec61970.generation.production.ProductionPackage#getFossilFuel_HighBreakpointP()
	 * @model dataType="iec61970.domain.ActivePower"
	 * @generated
	 */
    String getHighBreakpointP();

    /**
	 * Sets the value of the '{@link iec61970.generation.production.FossilFuel#getHighBreakpointP <em>High Breakpoint P</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>High Breakpoint P</em>' attribute.
	 * @see #getHighBreakpointP()
	 * @generated
	 */
    void setHighBreakpointP(String value);

    /**
	 * Returns the value of the '<em><b>Low Breakpoint P</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The active power output level of the unit at which the given type of fuel is switched off. This fuel (e.g., oil) is sometimes used to stabilize the base fuel (e.g., coal) at low active power output levels.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Low Breakpoint P</em>' attribute.
	 * @see #setLowBreakpointP(String)
	 * @see iec61970.generation.production.ProductionPackage#getFossilFuel_LowBreakpointP()
	 * @model dataType="iec61970.domain.ActivePower"
	 * @generated
	 */
    String getLowBreakpointP();

    /**
	 * Sets the value of the '{@link iec61970.generation.production.FossilFuel#getLowBreakpointP <em>Low Breakpoint P</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Low Breakpoint P</em>' attribute.
	 * @see #getLowBreakpointP()
	 * @generated
	 */
    void setLowBreakpointP(String value);

    /**
	 * Returns the value of the '<em><b>Fuel Allocation Schedule</b></em>' reference list.
	 * The list contents are of type {@link iec61970.generation.production.FuelAllocationSchedule}.
	 * It is bidirectional and its opposite is '{@link iec61970.generation.production.FuelAllocationSchedule#getFossilFuel <em>Fossil Fuel</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * A fuel allocation schedule must have a fossil fuel
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Fuel Allocation Schedule</em>' reference list.
	 * @see iec61970.generation.production.ProductionPackage#getFossilFuel_FuelAllocationSchedule()
	 * @see iec61970.generation.production.FuelAllocationSchedule#getFossilFuel
	 * @model opposite="FossilFuel"
	 * @generated
	 */
    EList<FuelAllocationSchedule> getFuelAllocationSchedule();

    /**
	 * Returns the value of the '<em><b>Thermal Generating Unit</b></em>' reference.
	 * It is bidirectional and its opposite is '{@link iec61970.generation.production.ThermalGeneratingUnit#getFossilFuels <em>Fossil Fuels</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * A thermal generating unit may have one or more fossil fuels
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Thermal Generating Unit</em>' reference.
	 * @see #setThermalGeneratingUnit(ThermalGeneratingUnit)
	 * @see iec61970.generation.production.ProductionPackage#getFossilFuel_ThermalGeneratingUnit()
	 * @see iec61970.generation.production.ThermalGeneratingUnit#getFossilFuels
	 * @model opposite="FossilFuels" required="true"
	 * @generated
	 */
    ThermalGeneratingUnit getThermalGeneratingUnit();

    /**
	 * Sets the value of the '{@link iec61970.generation.production.FossilFuel#getThermalGeneratingUnit <em>Thermal Generating Unit</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Thermal Generating Unit</em>' reference.
	 * @see #getThermalGeneratingUnit()
	 * @generated
	 */
    void setThermalGeneratingUnit(ThermalGeneratingUnit value);
}
