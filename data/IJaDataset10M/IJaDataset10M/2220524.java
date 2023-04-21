/*
 * Created on 15.01.2008
 */
package mipt.crec.lab.compmath.slae;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JOptionPane;

import mipt.aaf.edit.form.data.ChoiceObjectByDataCreator;
import mipt.aaf.edit.form.data.ObjectByDataCreator;
import mipt.crec.lab.common.modules.gui.parts.ComboComponentFieldEditor.ComboBoxDataModel;
import mipt.crec.lab.common.modules.math.IterativeControllerContainerModule;
import mipt.crec.lab.compmath.slae.gui.SLAEFormView;
import mipt.crec.lab.data.DataModule;
import mipt.crec.lab.gui.AbstractModuleView;
import mipt.crec.lab.gui.Resources;
import mipt.data.AbstractData;
import mipt.data.Data;
import mipt.data.DataSet;
import mipt.data.DataWrapper;
import mipt.data.MutableData;
import mipt.gui.OptionPane;
import mipt.math.Number;
import mipt.math.array.AbstractMatrix;
import mipt.math.array.BandedMatrix;
import mipt.math.array.spectrum.ManualSpectrumAlgorithm;
import mipt.math.array.spectrum.PowSpectrumAlgorithm;
import mipt.math.array.spectrum.SpectrumBoundsMethod;
import mipt.math.sys.ae.AEProblemSolver;
import mipt.math.sys.ae.LinAEProblem;
import mipt.math.sys.num.ConditionNumberOperator;
import mipt.math.sys.num.NormOperator;
import mipt.math.sys.num.SecondNormOperator;
import mipt.math.sys.num.Solver;
import mipt.math.sys.num.ae.AESolver;
import mipt.math.sys.num.ae.iter.IterativeAEAlgorithm;
import mipt.math.sys.num.ae.iter.IterativeAESolver;
import mipt.math.sys.num.ae.iter.SimpleIterativeAEAlgorithm;
import mipt.math.sys.num.ae.iter.ThreeLayeredIterativeAEAlgorithm;
import mipt.math.sys.num.ae.iter.param.IterativeParameter;
import mipt.math.sys.num.ae.iter.param.OptimalParameterMethod;
import mipt.math.sys.num.manage.Manageable;

/**
 * Concrete realization for SLAE.
 *
 * @author Korchak Anton
 */
public class SLAEControllerContainerModule extends IterativeControllerContainerModule<AESolver, LinAEProblem> {
	
	/**
	 * Идентификатор данных о спектре в Data.
	 */
	public static final String SPECTRUM = "spectrum";
	
	/**
	 * Идентификатор времени расчета границ спектра.
	 */
	public static final String SPECTRUM_CALCULATION_TIME = "spectrumTime";
	
	/**
	 * Идентификатор времени расчета нормы матрицы.
	 */
	public static final String NORMA_CALCULATION_TIME = "normaTime";
	
	/**
	 * Идентификатор времени расчета числа обусловленности матрицы.
	 */
	public static final String CONDITION_NUMBER_CALCULATION_TIME = "conditionNumberTime";
	
	/**
	 * Идентификатор времени получения дополнительных свойств матрицы.
	 */
	public static final String PROPERTY_CALCULATION_TIME = "propertyTime";
	
	/**
	 * The creator of object using Data.
	 */
	private ObjectByDataCreator objectCreator = null;
	
	/**
	 * Here initialization of controls (buttons).
	 * @see mipt.crec.lab.common.containers.data.DataContainerModule#setData(mipt.data.DataWrapper)
	 */
	public void setData(DataWrapper data) {
		super.setData(data);
		//Here we can adjust buttons.
		initControls();
	}
	
	/**
	 * @see mipt.crec.lab.common.containers.data.MultiDataContainerModule#getDataField(mipt.crec.lab.data.DataModule, mipt.data.DataWrapper)
	 */
	protected DataWrapper getDataField(DataModule child, DataWrapper parentData) {
		if (child instanceof SolutionModule) return parentData;
		return super.getDataField(child, parentData);
	}
	
	/**
	 * @see mipt.crec.lab.common.modules.math.ControllerContainerModule#start(boolean)
	 */
	public void start(boolean dataChanged) {
		firstStart = false;
		actionTrigger = true;
		//Определяем режим проведения вычислений.
		DataSet methods = getData().getData(SystemModule.DATA_NAME).getDataSet("method");
		Data method = ChoiceObjectByDataCreator.getSelectedData(methods);
		boolean isAlive = calculationThread != null && getCalculationThread().isAlive();
		boolean stepMode = method.getBoolean("stepMode");
		if (isPaused()) {//can be only if isAlive==true
			disablePause();
			getSolverPreprocessor().getStopper().resume();
			if (stepMode) nextIteration();//чтобы 2 раза не нажимать - сразу делаем следующую итерацию
			return;
		}
		if (stepMode && isAlive && !dataChanged){
			nextIteration();
			return;
		}
		//Else we should refresh the solution. The same situation for initial state.
		//Обнуляем решение в Data.
		final Data result = getData().getData(RESULT);
		resetSolution(result);
		dataChanged(result, false);
		if (isAlive) {
			getSolverPreprocessor().getStopper().resume();
			stop(); //Пока ссылка на старый солвер.
			solverStopper = null;
		}
		if (thread != null) stop(thread);//waitFor(thread);//waitFor results in deadlock in case of OptionPane.*
			//while(thread.isAlive());//TO DO: непротестированное место.
		setPreprocessorData(SystemModule.DATA_NAME);

		Solver solver = getSolver();//Создается новый солвер.
		//Передает текущие методы вычисления границ спектра (и нормы).
		if (solver instanceof IterativeAESolver) {
			IterativeAEAlgorithm algorithm = ((IterativeAESolver) solver).getIterativeAlgorithm();
			if (algorithm instanceof SimpleIterativeAEAlgorithm) {
				IterativeParameter parameter = ((SimpleIterativeAEAlgorithm) algorithm).getTau();
				if (parameter instanceof OptimalParameterMethod) ((OptimalParameterMethod) parameter).setSpectrumMethod(getSpectrumMethod());
				if (algorithm instanceof ThreeLayeredIterativeAEAlgorithm) {
					parameter = ((ThreeLayeredIterativeAEAlgorithm) algorithm).getAlfa();
					if (parameter instanceof OptimalParameterMethod) ((OptimalParameterMethod) parameter).setSpectrumMethod(getSpectrumMethod());
				}
			}
		}
		if (calculationThread != null && solver instanceof Manageable)
			getSolverPreprocessor().removeStopper(getSolverStopper());
		if (solver == null) return;
		//Время потраченное на вычисления.
		long calculationTime = -1;
		
		final MutableData inexactitude = (MutableData)result.getData(INEXACTITUDE);
		if (!method.getString(ComboBoxDataModel.NAME).equals("direct")) {
			//Тогда настраиваем режим проведения расчета.
			//Определяем "inexactitude".
			boolean createInexactSolution = method.getBoolean("createInexactSolution");
			if (!createInexactSolution) { //Проводим простое вычисление.
				//Замеряем время вычисления.
				calculationTime = solve(true);
				inexactitude.set(-1, ITERATION_COUNT);
				inexactitude.set(-1., DEVIATION_VALUE);
				finish(result, calculationTime);
			} else {
				//Определяем, учитывается ли ложная сходимость?
				Data convergence = ChoiceObjectByDataCreator.getSelectedData(method.getDataSet("convergence"));
				final boolean isFalseСonvergence = convergence.getString(ComboBoxDataModel.NAME).equals("falseConvergenceConsideration");
				//Оповещаем листенеров после каждой итерации.
				addSolverStopper(solver);
				calculationThread = null; //Сбрасывает нить вычисления.
				final long beginning = before();
				getCalculationThread().start(); //Запускаем вычисление.
				if (!stepMode) { //Если вычисления производятся без задержки.
					if (solver instanceof IterativeAESolver) { //Иначе не может быть.
						final IterativeAESolver solverIter = (IterativeAESolver) solver;
						thread = new Thread(new Runnable() {
							public void run() {
								int iteration = 0; //Число сделанных итераций.
								while (getCalculationThread().isAlive()) {
									Thread.yield();
									if (solverIter.getConvergenceAlgorithm().getInexactitude() != null) {
										int newIteration = solverIter.getConvergenceAlgorithm().getInexactitude().getIterationCount();
										if (newIteration > iteration) {
											iteration = newIteration;
											inexactitude.set(iteration, ITERATION_COUNT);
											inexactitude.set(solverIter.getConvergenceAlgorithm().getInexactitude().getDeviation().doubleValue(), DEVIATION_VALUE);
											if (isFalseСonvergence){
												Number q = solverIter.getConvergenceAlgorithm().getInexactitude().getRatio();
												if(q==null) q = Number.createScalar(-1);
												inexactitude.set(q.doubleValue(), Q_VALUE);
											}
											storeSolution(result, -1);
											dataChanged(result, false);
										}
										if (!isPaused())
											getSolverPreprocessor().getStopper().resume();
									}
								}
								thread = null;
								finish(result, after(beginning));
							}
						});
						thread.start();
					}
				}
			}
		} else { //Проводим простое вычисление.
			calculationTime = solve(true);
			finish(result, calculationTime);
		}
	}
	
	/**
	 * Adding button "Recalculate" of spectrum window.
	 * @see mipt.crec.lab.common.modules.math.IterativeControllerContainerModule#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("Пересчитать")) {
		startProperty();
		} else super.actionPerformed(e);
	}
	
	public void startProperty() {
		if (firstStart) {
			start();
			waitFor(getCalculationThread());
		}
		calcProperty();
	}
	
	/**
	 * Calculation of all matrix property such as spectrum and diagonal domination.
	 */
	protected void calcProperty(){
		double conditionNumberTime = -1;
		double normaTime = -1;
		double spectrumTime = -1;
		double propertyTime = -1;
		//Storage of spectrum information.
		Data spectrum = getData().getData(RESULT).getData(SPECTRUM);
		MutableData mutableSpectrum = (MutableData) spectrum;
		resetSpectrum(spectrum);
		try{
			setPreprocessorData(SystemModule.DATA_NAME);//to reinit matrix in getSolver()
			AEProblemSolver solver = getSolver(); //Because of Java 1.5
			AbstractMatrix matrix = (AbstractMatrix) ((LinAEProblem)solver.getAEProblem()).getMatrix();
			SpectrumModule module = (SpectrumModule) getModule(2);
			DataWrapper data = module.getData();
			//At this moment spectrum method fully initiated.
			SpectrumBoundsMethod spectrumMethod = module.spectrumMethod;
			spectrumMethod.setMatrix(matrix);
			//Setting bounds.
			AbstractData setter = (AbstractData) data.getData("spectrum");
			double time = System.nanoTime(); //Start of measurements.
			Number minValue = spectrumMethod.getMinEigenValue();
			Number maxValue = spectrumMethod.getMaxEigenValue();
			spectrumTime = System.nanoTime() - time; //End of measurements.
			if (spectrumMethod instanceof ManualSpectrumAlgorithm)
				storeSpectrum(mutableSpectrum, -1.0, SPECTRUM_CALCULATION_TIME);
			else storeSpectrum(mutableSpectrum, spectrumTime/1000000, SPECTRUM_CALCULATION_TIME);
			setter.set(minValue.toString(), "minValue");
			setter.set(maxValue.toString(), "maxValue");
			//Initialization of spectrum method if necessary (for second norm only).
			setter = (AbstractData) data.getData("norm");
			getObjectCreator().setData(ChoiceObjectByDataCreator.getSelectedData(setter.getDataSet("method")));
			NormOperator normMethod = (NormOperator) getObjectCreator().getObject();
			if (normMethod instanceof SecondNormOperator) ((SecondNormOperator) normMethod).setSpectrumBoundMethod(spectrumMethod);
			time = System.nanoTime(); //Start of measurements.
			Number norma = normMethod.calcNorm(matrix);
			norma.add(1);
			normaTime = System.nanoTime() - time; //End of measurements.
			setter.setDouble(norma.doubleValue(), "value");
			//Calculation of condition number.
			ConditionNumberOperator conditionNumberOperator = new ConditionNumberOperator();
			time = System.nanoTime(); //Start of measurements.
			Number conditionNumber = conditionNumberOperator.calcConditionNumber(matrix, normMethod);
			conditionNumberTime = System.nanoTime() - time; //End of measurements.
			setter.setDouble(conditionNumber.doubleValue(), "conditionNumber");
			setter = (AbstractData) data.getData("property");
			if (spectrumMethod instanceof PowSpectrumAlgorithm && !matrix.symmetric()) {
				String title = ((AbstractModuleView)getModule(2).getView()).getString("Error pow-method application title");
				String message = ((AbstractModuleView)getModule(2).getView()).getString("Error pow-method application message");
				JOptionPane.showMessageDialog(getView().getComponent(), message, title, JOptionPane.WARNING_MESSAGE);
			}
			if (setter.getBoolean("display")) {
				time = System.nanoTime(); //Start of measurements.
				String[] property = getProperty(matrix);
				propertyTime = System.nanoTime() - time; //End of measurements.
				storeSpectrum(mutableSpectrum, propertyTime/1000000, PROPERTY_CALCULATION_TIME);
				setter.set(property, "properties");
			} else {
				setter.set(new String[0], "properties");
				storeSpectrum(mutableSpectrum, -1.0, PROPERTY_CALCULATION_TIME);
			}
			
			storeSpectrum(mutableSpectrum, conditionNumberTime/1000000, CONDITION_NUMBER_CALCULATION_TIME);
			storeSpectrum(mutableSpectrum, normaTime/1000000, NORMA_CALCULATION_TIME);
			
			
			//Whole GUI should be updated.
			dataChanged(data, true);//Evdokimov: is the above thing true?
			//TO DO: update "Spectrum" tab
			//Update spectrum output.
			dataChanged(getData().getData(RESULT), true);
		}catch(RuntimeException e){//Jama can generate such kind of exceptions
			OptionPane.show(getView().getComponent(), new String[] {
				Resources.getInstance().getString(this, "Solution error")+":",
				e.getMessage()}, OptionPane.DEFAULT_OPTION, OptionPane.ERROR_MESSAGE);		}
	}
	
	protected String[] getProperty(AbstractMatrix matrix) {
		List<String> property = new ArrayList<String>(4);
		//Diagonal domination check.
		if (matrix.diagDomination()) property.add("Diagonal Domination");
		if (matrix.positiveDefiniteness()) property.add("Positive Definiteness");
		if (matrix.symmetric()) property.add("Symmetric");
		if (matrix instanceof BandedMatrix) property.add("Banded matrix, number of diagonals is " +
				((BandedMatrix)matrix).getNonzeroDiagonalCount());
		String[] array = new String[property.size()];
		return property.toArray(array);
	}
	
	/**
	 * Getting method to calculate spectrum bounds.
	 * @return method to calculate spectrum bounds. Not null.
	 */
	public final SpectrumBoundsMethod getSpectrumMethod() {
		return ((SpectrumModule) getModule(2)).spectrumMethod; //Not null.
	}
	
	/**
	 * Initialization of all necessary controls.
	 */
	protected void initControls() {
		//Настраиваем кнопку.
		JButton[] buttons = new JButton[] {((SLAEFormView)getModule(0).getView()).button,
				((SLAEFormView)getModule(2).getView()).button };
		//Настаивам Listener'ов.
		for (JButton button : buttons) {
			if (!Arrays.asList(button.getActionListeners()).contains(this)) {
				button.addActionListener(this);
			} if (!Arrays.asList(button.getKeyListeners()).contains(this)) {
				button.addKeyListener(this);
			}
		}
	}
	
	/**
	 * Getting creator of object by Data.
	 * @return creator of object by Data. Not null.
	 */
	protected ObjectByDataCreator getObjectCreator() {
		if (objectCreator == null) objectCreator = new ObjectByDataCreator();
		return objectCreator;
	}
	
	/**
	 * Store necessary field to spectrum {@link Data}.
	 * @param value - value of field.
	 * @param field - name of field.
	 */
	protected void storeSpectrum(MutableData spectrum, Object value, String field) {
		spectrum.set(value, field);
	}
	
	/**
	 * Refreshing result data about spectrum.
	 * @param spectrum - data with spectrum result.
	 */
	protected void resetSpectrum(Data spectrum) {
		MutableData data = (MutableData) spectrum;
		data.setDouble(-1, SPECTRUM_CALCULATION_TIME);
		data.setDouble(-1, NORMA_CALCULATION_TIME);
		data.setDouble(-1, CONDITION_NUMBER_CALCULATION_TIME);
		data.setDouble(-1, PROPERTY_CALCULATION_TIME);
	}
}
