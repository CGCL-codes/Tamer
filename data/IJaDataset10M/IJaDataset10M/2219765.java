package saf.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import saf.ast.Bot;
import saf.ast.Checker;
import saf.ast.action.Choose;
import saf.ast.action.Function;
import saf.ast.action.Parameters;
import saf.utils.Config;

public class State {
	
	private Bot _fighter;
	private State _opponentState;
	private SelectBehaviour _fighterBehaviour; 
	private Integer _position;
	private ActionPair _actionPair;
	private Integer _health;
	private Checker checkFighter;
	private static final Random RANDOM = new Random();
	private ImageType _imageType;
	
	public enum ImageType{
		Normal,
		Flipped
	}
	
	public State(Bot fighter, Integer position){
	
		this._fighter = fighter;
		this.setPosition(position);
		this.setHealth(Config.DEFAULTHEALTH);
		checkFighter = new Checker();
		_fighter.accept(checkFighter);
		_fighterBehaviour = new SelectBehaviour(_fighter);
		_actionPair = new ActionPair("", "");
		_imageType = position == 0 ? ImageType.Normal: ImageType.Flipped;
		
	}

	public List<String> getErrorMessages(){
		return checkFighter.getErrorMessages();
	}
	
	public Integer getPosition() {
		return _position;
	}

	private void setPosition(Integer _position) {
		this._position = _position;
	}
	
	public ActionPair getActionPair(){
		return _actionPair;
	}

	public Integer getHealth() {
		return _health;
	}

	private void setHealth(Integer _health) {
		this._health = _health;
	}
	
	public Bot getFighter(){
		return _fighter;
	}

	public State getNextState(State opponentState){
		this._opponentState = opponentState;
		determineNextState();
		return this;
	}
	
	private void determineNextState(){
		
		//Ideal conditions
		String reachCondition = determineReachCondition();
		String strengthCondition = determineStrengthCondition();
		
		Function applyCondition;
		
		if(reachCondition.equals("far")){
			applyCondition = _fighterBehaviour.GetFunction(reachCondition);
		}else{
			applyCondition = _fighterBehaviour.GetFunction(strengthCondition);
			if(applyCondition.getCondition().equals("always"))
				applyCondition = predictCondition(reachCondition, strengthCondition);
			
		}
		
		_actionPair = getActionPair(applyCondition);
	}

	private Function predictCondition(String reachCondition, String strengthCondition){
		
		List<Function> allFunction =  _fighterBehaviour.getAllFunctions();
		for(Function condition : allFunction){
			ActionPair actionPair = getActionPair(condition);
			if(actionPair.getMove().contains("_")){
				if(checkConditionRange(strengthCondition, condition.getCondition()))
					return condition;
			}
		}
		
		return _fighterBehaviour.GetFunction("near");
	} 
	
	private boolean checkConditionRange(String conditionFirst, String conditionSecond){
		return conditionFirst.contains(conditionSecond) || conditionSecond.contains(conditionFirst);
	}
	
	private ActionPair getActionPair(Function condition){
		
		ActionPair returnPair = new ActionPair("", "");
		Parameters parameter = condition.getParameters();
		switch (parameter.getParameterType()) {
			case IdentAndIdent:
				returnPair = new ActionPair(parameter.getFirstConditon(), parameter.getSecondCondition());
			break;	
			case IdentAndChoose:
				String chooseSecond = (String)selectRandomObject(getChooseAsList(parameter.getSecondChoose()));
				returnPair = new ActionPair(parameter.getFirstConditon(), chooseSecond);
				break;
			case ChooseAndIdent:
				String chooseFirst = (String)selectRandomObject(getChooseAsList(parameter.getFirstChoose()));
				returnPair = new ActionPair(chooseFirst, parameter.getSecondCondition());
				break;
			default:
				String chooseFirst1 = (String)selectRandomObject(getChooseAsList(parameter.getFirstChoose()));
				String chooseSecond1 = (String)selectRandomObject(getChooseAsList(parameter.getSecondChoose()));
				returnPair = new ActionPair(chooseFirst1, chooseSecond1);
				break;
		}
		
		return returnPair;
	}
	
	private ArrayList<Object> getChooseAsList(Choose choose){
		ArrayList<Object> chooseOptions = new ArrayList<Object>();
		chooseOptions.add(choose.getActions());
		return chooseOptions;
	}
	
	private String determineStrengthCondition(){
		
		String condition = Config.DEFAULTCONDITION;
		if(_health == _opponentState.getHealth()){
			condition = "even";
		}
		else if(_health < 10){
			condition = "much_weaker";
		}else if(_health > 10 && _health < 30){
			condition = "weaker";
		}else if(_health > 90){
			condition = "much_stronger";
		}else if(_health < 90 && _health > 70){
			condition = "stronger";
		}
		
		return condition;
	}
	
	private String determineReachCondition(){
		
		int reachDifference = Math.abs(this._position - _opponentState.getPosition());
		if(isWithinReach(reachDifference)) return "near";
		return "far";
		
	}
	
	private boolean isWithinReach(int reachDifference){
		
		//check for punch reach
		int punchReach = _fighterBehaviour.getCharactersticValue("punchReach");
		int kickReach = _fighterBehaviour.getCharactersticValue("kickReach");
		return (punchReach >= reachDifference) || (kickReach >= reachDifference);
		
	}
	
	private Object selectRandomObject(List<Object> list){
		return list.get(RANDOM.nextInt(list.size()));
	}
	
	public void Fight(State opponentState){
		
		//update self position
		_position = getNewPosition(this, opponentState.getPosition());
		
		//update opponent position
		opponentState._position = getNewPosition(opponentState, this.getPosition());
		
		//do action self 
		checkAndApplyAction(this);
		
		//do action opponent
		checkAndApplyAction(opponentState);
	}
	
	private void checkAndApplyAction(State state){
		
		int reach = 0;
		if(state.getActionPair().getFight().contains("punch")) reach = state._fighterBehaviour.getCharactersticValue("punchReach");
		else if (state.getActionPair().getFight().contains("kick")) reach = state._fighterBehaviour.getCharactersticValue("kickReach");
		
		int newReachDifference = Math.abs(state.getPosition() - state._opponentState.getPosition());
		if(reach >= newReachDifference){
			//check fight action
			int fightPoints  = getFightPoints(state);
		    state._opponentState._health -= fightPoints;
		    if(state._opponentState._health < 0) state._opponentState._health = 0;
		}
		
	}
	
	private int getNewPosition(State state, int limit){
		
		int movePosition = getMovePosition(state.getActionPair().getMove());
		
		int tempMovePosition = 0;
		switch (state._imageType) {
			case Normal:
				tempMovePosition = state.getPosition() + movePosition;
				
				if(tempMovePosition < limit && tempMovePosition > 0 && tempMovePosition < Config.INITIALPOSITION)
					return tempMovePosition;
				
				break;
			default:
				tempMovePosition = state.getPosition() - movePosition;
				
				if(tempMovePosition > limit && tempMovePosition > 0  && tempMovePosition < Config.INITIALPOSITION)
					return tempMovePosition;
				
				break;
		}
		
		
		return state.getPosition();
	}
	
	private int getMovePosition(String moveAction){
		
		moveAction = moveAction.toLowerCase();
		if(moveAction.equals("run_towards")) return 2;
		else if(moveAction.equals("run_away")) return -2;
		else if(moveAction.equals("walk_towards")) return 1;
		else if(moveAction.equals("walk_away")) return -1;
		else return 0;
		
	}
	
	private int getFightPoints(State state){
		
		String fightAction = state.getActionPair().getFight();
		if(fightAction.contains("punch")) return state._fighterBehaviour.getCharactersticValue("punchPower");
		else if(fightAction.contains("kick")) return state._fighterBehaviour.getCharactersticValue("kickPower");
		else return Config.DEFAULTBLOCKHEALTH; 
		
	}

	public ImageType getImageType() {
		return _imageType;
	}

	public void setÌmageType(ImageType _imageType) {
		this._imageType = _imageType;
	}
	
}
