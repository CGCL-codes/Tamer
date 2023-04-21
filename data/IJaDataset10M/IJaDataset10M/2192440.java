package com.gorillalogic.modelx.usecase;

import com.gorillalogic.dal.*;
import com.gorillalogic.dal.model.ModelElement;
import com.gorillalogic.dal.model.ScopedElement;
import com.gorillalogic.gosh.Gosh;
import com.gorillalogic.modelx.usecase.common.CommonUsecase;

/**
 * <code>Usecase</code> is a behavioral description of an Actor's 
 * interaction with the system
 *
 * GXE Usecases describe their availabilty using <code>StartFlow</code>
 * objects that describe the UI circumstances under which the use case 
 * may be made available

<pre>
---------------------------------------------------------------
The Usecase API
---------------

Getting all usecases which can be initialized:
	These methods come in two forms, "Absolute" and "Relative". The "Absolute" 
   	methods take a screenIdentifier, such as "home", or "anywhere"; the 
	"Relative" methods take a context, i.e. a dal table.

	The "Absolute" forms work with startFlows of the form

		startFlow from <ScreenId> with <context> 

	if the startFlow's screenId matches the one supplied, or is "anywhere", 
	the Usecase will be evaluated for eligibility, i.e. the preconditions will 
	be checked. If the Usecase is initiated, the initialContext 
	will be "<context>"

	The "Relative" forms will interrogate startFlows of the form

		startFlow from <expr> 

	if the GCL expression "<supplied context> in <expr>" is true, the Usecase 
	will be evaluated for eligibility, i.e. the preconditions will be checked. 
	If the Usecase is initiated, the initialContext will 
	be "<supplied context>[in <expr>]"

	These are the only two forms of startFlow currently supported.

---------------------------------------------------------------
Finding and Initiating Usecases
-------------------------------

Get all usecases which can be initiated:
	Usecase.factory.getAllInitiatableAbsolute(String screenIdentifier) 
            throws AccessException;
	Usecase.factory.getAllInitiatableAbsolute(Table context) 
            throws AccessException;
       - returns a Usecase (i.e. a table of Usecase.Row(s), each representing 
         an initiatable usecase
       - if there are no initiatable usecases, returns an empty Usecase
       - exceptions only if thrown from dal, e.g. startFlow specifies 
         invalid context for preconditions

Checking if a particular usecase can be initiated:
	boolean   canInitiateAbsolute(String pageKind)  throws AccessException;
    boolean   canInitiateRelative(Table candidateContext)  
            throws AccessException;
       - exceptions only if thrown from dal, e.g. startFlow specifies 
         invalid context for preconditions

Initiate a usecase
    UsecaseInstance.Row initiateAbsolute(String from) throws AccessException;
    UsecaseInstance.Row initiateRelative(Table context) throws AccessException;
       - return a UsecaseInstance.Row, which can naviagate to its usecase and 
         current step, and maintains its current context
       - throws an exception if the usecase cannot be initiated, because there 
         is eligble startFlow (given the screenId or context) which produces 
         an initalContext which satisfies the preconditions
       - precondition violations are reported as ConstraintViolations 
         (sorry, just the first one right now)

---------------------------------------------------------------
Executing a Usecase
-------------------

The UsecaseInstance object is responsible for maintaining a "currentContext" 
for the usecase (as a string representation of a GCL path). All expressions in 
the Usecase State Machine (triggers, guard conditions, entry/exit/effect 
processing) will be evaluated/executed in this context. They may change the 
context. 

The UsecaseInstance object supplied by Usecase.Row.initiateAbsolute() or 
Usecase.Row.initiateAbsolute() will be initialized from a data point of view, 
i.e. its current Step() and FlowContext() should be evaluated:
    Step.Row    getStep() throws AccessException;
    Table       getFlowContextTable() throws AccessException;

These can be used to obtain direction in terms of display. Currently, the Step
class provides "space holders" for name, title, screenInfo, and 
additional preconditions, but these are not imported. 

For any Step.Row, there are certain Events which may trigger transitions. These
are available from the Step.Row:
	Event	events()   throws AccessException;
	
To progress through the steps of the usecase, dispatch the selected Event.Row
to the UsecaseInstance.Row:

	void	dispatch(Event.Row e);	

</pre>

 * 
 *  *  
 */
public interface Usecase extends ModelElement {

    public static final String PACKAGE_USECASE = "usecase";

    public static final String TYPE_USECASE = "Usecase";

    public static final String PATH_USECASE = "/" + PACKAGE_USECASE + "/" + TYPE_USECASE;

    public static final String ELEMENT_NAME = "name";

    public static final String ELEMENT_INSTANCEPATH = "instancePath";

    public static final String ELEMENT_STARTFLOW = "startFlow";

    public static final String ELEMENT_FROM = "from";

    public static final String ELEMENT_WITHCONTEXT = "withContext";

    public static final String ELEMENT_PRECOND = "precond";

    public static final String ELEMENT_POSTCOND = "postcond";

    public static final String ELEMENT_STEP = "step";

    public static final String ELEMENT_STATEMACHINE = "stateMachine";

    public static final String ELEMENT_BODY = "body";

    Row usecaseRow(long rowId) throws AccessException;

    Row usecaseRow(String path) throws AccessException;

    Itr usecaseItr() throws AccessException;

    public interface Row extends ScopedElement.Row {

        Usecase.Row asUsecaseRow() throws AccessException;

        String name() throws AccessException;

        String instancePath() throws AccessException;

        StartFlow startFlow() throws AccessException;

        String[] precond() throws AccessException;

        String[] postcond() throws AccessException;

        Step step() throws AccessException;

        boolean canInitiateAbsolute(String pageKind) throws AccessException;

        boolean canInitiateRelative(Table candidateContext) throws AccessException;

        Table.Row stateMachine() throws AccessException;

        UsecaseInstance.Row initiateAbsolute(String from, Gosh gosh) throws AccessException;

        UsecaseInstance.Row initiateRelative(Table context, Gosh gosh) throws AccessException;

        UsecaseInstance.Row initiateAbsolute(String from) throws AccessException;

        UsecaseInstance.Row initiateRelative(Table context) throws AccessException;
    }

    public interface Itr extends Row, ModelElement.Itr {

        Usecase.Row asUsecaseRow() throws AccessException;
    }

    public static interface Factory {

        Usecase.Row getUsecase(String path) throws AccessException;

        Usecase.Row getUsecaseFromOid(String oid) throws AccessException;

        Usecase.Row getUsecaseFromName(String name) throws AccessException;

        /** get all currently-initiatable usecases via "absolute" startFlows */
        Usecase getAllInitiatableAbsolute(String pageKind) throws AccessException;

        /** get all currently-initiatable usecases via "relative" startFlows */
        Usecase getAllInitiatableRelative(Table context) throws AccessException;
    }

    public static final Factory factory = new CommonUsecase.CommonUsecaseFactory();
}
