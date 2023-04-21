package eu.vph.predict.vre.in_silico.business.application.chaste.chaste_parameters;

/**
 * <ul>
 *   <li>time_type</li>
 *   <li>dimensionless_type</li>
 *   <li>conductivity_type</li>
 *   <li>stimulus_strength_type</li>
 *   <li>inverse_length_type</li>
 *   <li>capacitance_type</li>
 *   <li>location_type</li>
 *   <li>domain_type</li>
 *   <li>ionic_models_available_type</li>
 *   <li><b>+</b> ionic_model_selection_type</li>
 *   <li><b>+</b> dynamically_loaded_ionic_model_type</li>
 *   <li><b>+</b> path_type</li>
 *   <li><b>+</b> relative_to_type</li>
 *   <li>ionic_models_region_type</li>
 *   <li>ionic_models_type</li>
 *   <li>apd_map_type</li>
 *   <li>upstrokes_map_type</li>
 *   <li><b>+</b> max_upstrokes_velocity_map_type</li>
 *   <li><b>-</b> empty_type</li>
 *   <li>conduction_velocity_map_type</li>
 *   <li>media_type</li>
 *   <li>point_type</li>
 *   <li>box_type</li>
 *   <li>stimuus_type</li>
 *   <li>cell_heterogeneity_type</li>
 *   <li>conductivity_heterogeneity_type</li>
 *   <li>slab_type</li>
 *   <li>sheet_type</li>
 *   <li>fibre_type</li>
 *   <li>load_mesh_type</li>
 *   <li>mesh_type</li>
 *   <li>conductivities_type</li>
 *   <li><b>+</b> var_type</li>
 *   <li><b>+</b> output_variables_type</li>
 *   <li><b>+</b> yesno_type</li>
 *   <li><b>+</b> output_visualizer_type</li>
 *   <li>time_steps_type</li>
 *   <li><b>-</b> ksp_use_type</li>
 *   <li>ksp_tolerences_type</li>
 *   <li>ksp_solver_type</li>
 *   <li>ksp_preconditioner_type</li>
 *   <li><b>+</b> checkpoint_type</li>
 *   <li><b>+</b> adaptivity_parameters_type</li>
 *   <li>simulation_type</li>
 *   <li>physiological_type</li>
 *   <li>numerical_type</li>
 *   <li>postprocessing_type</li>
 *   <li>chaste_parameters_type</li>
 * </ul>
 *
 * @author Geoff Williams
 */
public interface ChasteParametersInterfaceV2_0 extends ChasteParametersInterfaceV1_1 {

    /**
   * Specify an ionic model hardcoded into Chaste and chosen from a list. Currently one of :
   * <ul>
   *   <li>Fox2002BackwardEuler</li>
   *   <li>LuoRudyIBackwardEuler</li>
   *   <li>LuoRudyI</li>
   *   <li>FaberRudy2000Optimised</li>
   *   <li>FaberRudy2000</li>
   *   <li>DifrancescoNoble</li>
   *   <li>MahajanShiferaw</li>
   *   <li>HodgkinHuxley</li>
   *   <li>tenTusscher2006</li>
   *   <li>Maleckar</li>
   * </ul>.
   * 
   * @param ionicModelsDefaultHardcoded Default hardcoded ionic model.
   */
    public void assignIonicModelsDefaultHardcoded(final String ionicModelsDefaultHardcoded);
}
