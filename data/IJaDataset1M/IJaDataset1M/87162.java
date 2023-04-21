package org.exist.eclipse.auto.internal.result.control;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.exist.eclipse.auto.internal.exception.AutoException;
import org.exist.eclipse.auto.internal.result.model.IResultModel;
import org.exist.eclipse.auto.internal.result.model.ResultModelConverter;

/**
 * Provides the data for the table in the automation result.
 * 
 * @author Markus Tanner
 */
public class ResultContentProvider implements IStructuredContentProvider {

    private String _inputData;

    private IResultModel _model = null;

    /**
	 * ResultContentProvider Constructor
	 * 
	 * @param inputData
	 */
    public ResultContentProvider(String inputData) {
        _inputData = inputData;
    }

    public Object[] getElements(Object inputElement) {
        try {
            return getModel().getContents();
        } catch (AutoException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
	 * Returns the IResultModel if it's not istantiated yet.
	 * 
	 * @return
	 * @throws AutoException
	 */
    public IResultModel getModel() throws AutoException {
        if (_model == null) {
            _model = ResultModelConverter.getResultModel(_inputData);
        }
        return _model;
    }

    public void dispose() {
        _model = null;
    }

    public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
    }
}
