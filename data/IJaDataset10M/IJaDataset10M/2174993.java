package uk.ac.lkl.expresser.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import uk.ac.lkl.common.util.expression.LocatedExpression;
import uk.ac.lkl.common.util.value.IntegerValue;
import uk.ac.lkl.migen.system.expresser.model.ExpresserModel;
import uk.ac.lkl.migen.system.expresser.model.ModelColor;
import uk.ac.lkl.migen.system.expresser.model.shape.block.BlockShape;
import uk.ac.lkl.migen.system.expresser.model.shape.block.PatternShape;
import uk.ac.lkl.migen.system.expresser.model.tiednumber.TiedNumberExpression;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Handles communication with the server for events
 * 
 * @author Ken Kahn
 *
 */
public class EventManager {

    private ExpresserCanvas canvas;

    public EventManager(ExpresserCanvasPanel expresserCanvasPanel) {
        super();
        canvas = expresserCanvasPanel.getExpresserCanvas();
    }

    private HashMap<String, LocatedExpression<IntegerValue>> idToExpressionMap = new HashMap<String, LocatedExpression<IntegerValue>>();

    public void tileDropped(final TileView tileView, ExpresserCanvas canvas) {
        final BlockShape patternShape;
        String id = tileView.getId();
        if (id == null) {
            ExpresserModel model = canvas.getModel();
            patternShape = tileView.getPatternShape(canvas);
            model.addObject(patternShape);
            patternShape.setSelected(true);
        } else {
            patternShape = tileView.getPatternShape(canvas);
        }
        int x = tileView.getModelX(canvas);
        int y = tileView.getModelY(canvas);
        putTileInDataStore(tileView, x, y);
        if (patternShape != null) {
            patternShape.moveTo(new IntegerValue(x), new IntegerValue(y));
        }
    }

    /**
     * @param tileView
     * @param patternShape
     * @param originalId

     * @param x
     * @param y
     */
    public void putTileInDataStore(final TileView tileView, int x, int y) {
        AsyncCallback<String> callback = new AsyncCallback<String>() {

            @Override
            public void onFailure(Throwable caught) {
                Utilities.logException(caught, "Failed to contact server to report a tile drop.");
            }

            @Override
            public void onSuccess(String result) {
                if (result != null) {
                    Utilities.severe("Server reported: " + result);
                }
            }
        };
        String id = tileView.getId();
        Expresser.getExpresserService().tileCreatedOrUpdated(x, y, tileView.getColorName(), id, Expresser.getUserKey(), Configuration.getProjectName(), Configuration.getXmppLogRecipients(), callback);
    }

    public void groupShapeDropped(final GroupShapeView groupShapeView, ExpresserCanvas expresserCanvas) {
        AsyncCallback<String> callback = new AsyncCallback<String>() {

            @Override
            public void onFailure(Throwable caught) {
                Utilities.logException(caught, "Failed to contact server to report a group shape drop.");
            }

            @Override
            public void onSuccess(String result) {
                if (result != null) {
                    Utilities.severe("Server reported: " + result);
                }
            }
        };
        String id = groupShapeView.getId();
        if (id == null) {
            Utilities.severe("id is null for move group shape");
            return;
        }
        BlockShape shape = moveCorrespondingShape(id, groupShapeView, expresserCanvas);
        Expresser.getExpresserService().groupShapeMoved(shape.getX(), shape.getY(), id, Expresser.getUserKey(), Configuration.getProjectName(), callback);
    }

    public void patternMoved(final ShapeView patternView, ExpresserCanvas canvas) {
        AsyncCallback<String> callback = new AsyncCallback<String>() {

            @Override
            public void onFailure(Throwable caught) {
                Utilities.logException(caught, "Failed to contact server to report a pattern drop.");
            }

            @Override
            public void onSuccess(String result) {
                if (result != null) {
                    Utilities.severe("Server reported: " + result);
                }
            }
        };
        String id = patternView.getId();
        if (id == null) {
            Utilities.severe("id is null for move pattern");
            return;
        }
        BlockShape shape = moveCorrespondingShape(id, patternView, canvas);
        int x;
        int y;
        if (shape instanceof PatternShape) {
            BlockShape baseShape = ((PatternShape) shape).getShape();
            x = baseShape.getX();
            y = baseShape.getY();
        } else {
            x = shape.getX();
            y = shape.getY();
        }
        Expresser.getExpresserService().patternMoved(x, y, id, Expresser.getUserKey(), Configuration.getProjectName(), callback);
    }

    public void expressionCreatedOrUpdated(final ExpressionPanel expressionPanel, ExpresserCanvas canvas) {
        int x = expressionPanel.getAbsoluteLeft() - canvas.getAbsoluteLeft();
        int y = expressionPanel.getAbsoluteTop() - canvas.getAbsoluteTop();
        AsyncCallback<String> callback = new AsyncCallback<String>() {

            @Override
            public void onFailure(Throwable caught) {
                Utilities.logException(caught, "Failed to contact server to report expression creation.");
            }

            @Override
            public void onSuccess(String result) {
                if (result != null) {
                    Utilities.severe(result);
                }
            }
        };
        String xml = expressionPanel.getXML(this);
        String id = expressionPanel.getId();
        if (id == null) {
            id = Utilities.getGuid();
            expressionPanel.setId(id);
        }
        Expresser.getExpresserService().expressionCreatedOrUpdated(id, x, y, xml, Expresser.getUserKey(), Configuration.getProjectName(), callback);
    }

    public void updateTiedNumber(TiedNumberExpression<IntegerValue> tiedNumber) {
        String idString = tiedNumber.getIdString();
        if (idString == null || idString.isEmpty()) {
            idString = Utilities.getGuid();
            tiedNumber.setIdString(idString);
        }
        AsyncCallback<String> callback = new AsyncCallback<String>() {

            @Override
            public void onFailure(Throwable caught) {
                Utilities.logException(caught, "Failed to contact server to report a tied number update.");
            }

            @Override
            public void onSuccess(String result) {
                if (result != null) {
                    Utilities.severe("Server reported: " + result);
                }
            }
        };
        Expresser.getExpresserService().updateTiedNumber(idString, tiedNumber.evaluate().getInt(), tiedNumber.getName(), tiedNumber.isNamed(), tiedNumber.getDisplayMode(), tiedNumber.isLocked(), tiedNumber.isKeyAvailable(), callback);
    }

    public void updateTotalTilesExpression(ExpressionPanel newEpressionPanel) {
        AsyncCallback<String> callback = new AsyncCallback<String>() {

            @Override
            public void onFailure(Throwable caught) {
                Utilities.logException(caught, "Failed to contact server to report a tied number update.");
            }

            @Override
            public void onSuccess(String result) {
                if (result != null) {
                    Utilities.severe("Server reported: " + result);
                }
            }
        };
        Expresser.getExpresserService().updateTotalTilesExpression(newEpressionPanel.getXML(this), Expresser.getUserKey(), Configuration.getProjectName(), callback);
    }

    public void updateColorSpecificRuleExpression(ModelColor color, ExpressionPanel newEpressionPanel) {
        AsyncCallback<String> callback = new AsyncCallback<String>() {

            @Override
            public void onFailure(Throwable caught) {
                Utilities.logException(caught, "Failed to contact server to report a tied number update.");
            }

            @Override
            public void onSuccess(String result) {
                if (result != null) {
                    Utilities.severe("Server reported: " + result);
                }
            }
        };
        Expresser.getExpresserService().updateColorSpecificRuleExpression(newEpressionPanel.getXML(this), color.toHTMLString(), Expresser.getUserKey(), Configuration.getProjectName(), callback);
    }

    public void expressionDropped(final ExpressionPanel expressionPanel, ExpresserCanvas canvas) {
        int x = expressionPanel.getAbsoluteLeft() - canvas.getAbsoluteLeft();
        int y = expressionPanel.getAbsoluteTop() - canvas.getAbsoluteTop();
        String id = expressionPanel.getId();
        AsyncCallback<String> callback = new AsyncCallback<String>() {

            @Override
            public void onFailure(Throwable caught) {
                Utilities.logException(caught, "Failed to contact server to report an expression drop.");
            }

            @Override
            public void onSuccess(String result) {
                if (result != null) {
                    Utilities.severe("Server reported: " + result);
                }
            }
        };
        Expresser.getExpresserService().expressionMoved(x, y, id, Expresser.getUserKey(), Configuration.getProjectName(), callback);
    }

    /**
     * @param id
     * @param shapeView
     * @param canvas
     * 
     * Moves the corresponding model shape by the amount just moved
     * @return the BlockShape corresponding to the shapeView
     */
    protected BlockShape moveCorrespondingShape(String id, final ShapeView shapeView, ExpresserCanvas canvas) {
        BlockShape shape = shapeView.getPatternShape(canvas);
        IntegerValue x = new IntegerValue(shapeView.getModelX(canvas));
        IntegerValue y = new IntegerValue(shapeView.getModelY(canvas));
        shape.moveTo(x, y);
        return shape;
    }

    public void shapeDeleted(ShapeView shapeView, ExpresserCanvasPanel expresserCanvasPanel) {
        AsyncCallback<String> callback = new AsyncCallback<String>() {

            @Override
            public void onFailure(Throwable caught) {
                Utilities.logException(caught, "Failed to contact server to report a shape deletion.");
            }

            @Override
            public void onSuccess(String result) {
                if (result != null) {
                    Utilities.severe(result);
                }
            }
        };
        String id = shapeView.getId();
        if (id != null) {
            Expresser.getExpresserService().shapeDeleted(id, Expresser.getUserKey(), Configuration.getProjectName(), callback);
            BlockShape blockShape = shapeView.getPreExistingPatternShape();
            if (blockShape != null) {
                expresserCanvasPanel.getModel().removeObject(blockShape);
            }
        }
    }

    public void expressionDeleted(ExpressionPanel expressionPanel, ExpresserCanvas expresserCanvas) {
        AsyncCallback<String> callback = new AsyncCallback<String>() {

            @Override
            public void onFailure(Throwable caught) {
                Utilities.logException(caught, "Failed to contact server to report a shape deletion.");
            }

            @Override
            public void onSuccess(String result) {
                if (result != null) {
                    Utilities.severe(result);
                }
            }
        };
        String id = expressionPanel.getId();
        if (id != null) {
            Expresser.getExpresserService().expressionDeleted(id, Expresser.getUserKey(), Configuration.getProjectName(), callback);
            LocatedExpression<IntegerValue> locatedExpression = idToExpressionMap.get(id);
            if (locatedExpression != null) {
                expresserCanvas.getModel().removeLocatedExpression(locatedExpression);
                idToExpressionMap.remove(id);
            }
        }
    }

    public void shapeCopied(final ShapeView shapeView, final ShapeView copy, ExpresserCanvas canvas) {
        String id = shapeView.getId();
        if (id == null) {
            id = Utilities.getGuid();
            shapeView.setId(id);
        }
        String idOfCopy = copy.getId();
        if (idOfCopy == null) {
            idOfCopy = Utilities.getGuid();
            copy.setId(idOfCopy);
        }
        ExpresserModel model = canvas.getModel();
        PatternShape patternShape = copy.getPatternShape();
        model.addObject(patternShape);
        patternShape.setSelected(true);
        int x = copy.getModelX(canvas);
        int y = copy.getModelY(canvas);
        patternShape.moveTo(new IntegerValue(x), new IntegerValue(y));
        AsyncCallback<String> callback = new AsyncCallback<String>() {

            @Override
            public void onFailure(Throwable caught) {
                Utilities.logException(caught, "Failed to contact server to report the copying of a shape.");
            }

            @Override
            public void onSuccess(String result) {
                if (result != null) {
                    Utilities.severe(result);
                }
            }
        };
        Expresser.getExpresserService().shapeCopied(id, idOfCopy, Expresser.getUserKey(), Configuration.getProjectName(), x, y, callback);
    }

    public void groupCreated(int x, int y, final GroupShapeView groupShapeView) {
        ExpresserModel model = canvas.getModel();
        final PatternShape patternShape = groupShapeView.getPatternShape(canvas);
        model.addObject(patternShape);
        int modelX = groupShapeView.getModelX(canvas);
        int modelY = groupShapeView.getModelY(canvas);
        patternShape.moveTo(new IntegerValue(modelX), new IntegerValue(modelY));
        putGroupInDataStore(groupShapeView, modelX, modelY);
    }

    public void putGroupInDataStore(GroupShapeView groupShapeView) {
        int modelX = groupShapeView.getModelX(canvas);
        int modelY = groupShapeView.getModelY(canvas);
        putGroupInDataStore(groupShapeView, modelX, modelY);
    }

    /**
     * @param groupShapeView
     * @param modelX
     * @param modelY
     */
    public void putGroupInDataStore(GroupShapeView groupShapeView, int modelX, int modelY) {
        ArrayList<String> subShapeIds = getSubShapeIds(groupShapeView);
        AsyncCallback<String> callback = new AsyncCallback<String>() {

            @Override
            public void onFailure(Throwable caught) {
                Utilities.logException(caught, "Failed to contact server to report the making of a group shape.");
            }

            @Override
            public void onSuccess(String result) {
                if (result != null) {
                    Utilities.severe(result);
                }
            }
        };
        String id = groupShapeView.getId();
        Expresser.getExpresserService().groupCreated(id, modelX, modelY, subShapeIds, Expresser.getUserKey(), Configuration.getProjectName(), callback);
    }

    /**
     * @param groupShapeView
     * @return
     */
    public ArrayList<String> getSubShapeIds(final GroupShapeView groupShapeView) {
        ArrayList<String> subShapeIds = new ArrayList<String>();
        List<ShapeView> subShapeViews = groupShapeView.getSubShapeViews();
        for (ShapeView subShapeView : subShapeViews) {
            String id = subShapeView.getId();
            if (id == null) {
                Utilities.severe("Subshape doesn't have an id");
            }
            subShapeIds.add(id);
        }
        return subShapeIds;
    }

    public void patternCreatedOrUpdated(int left, int top, final PatternView patternView) {
        ExpresserModel model = canvas.getModel();
        PatternShape patternShape = patternView.getPatternShape(canvas);
        model.addObject(patternShape);
        int modelX = patternView.getModelX(canvas);
        int modelY = patternView.getModelY(canvas);
        patternShape.moveTo(new IntegerValue(modelX), new IntegerValue(modelY));
        putPatternInDataStore(patternView, modelX, modelY);
    }

    public void putPatternInDataStore(PatternView patternView) {
        int modelX = patternView.getModelX(canvas);
        int modelY = patternView.getModelY(canvas);
        putPatternInDataStore(patternView, modelX, modelY);
    }

    /**
     * @param patternView
     * @param patternShape
     * @param modelX
     * @param modelY
     */
    public void putPatternInDataStore(PatternView patternView, int modelX, int modelY) {
        final AsyncCallback<String> callback = new AsyncCallback<String>() {

            @Override
            public void onFailure(Throwable caught) {
                Utilities.logException(caught, "Failed to contact server to report the making of a pattern.");
            }

            @Override
            public void onSuccess(String result) {
                if (result != null) {
                    Utilities.severe(result);
                }
            }
        };
        Expresser.getExpresserService().patternCreatedOrUpdated(patternView.getId(), patternView.getBuildingBlock().getId(), modelX, modelY, patternView.getXML(this), Expresser.getUserKey(), Configuration.getProjectName(), callback);
    }

    public void patternUnmade(PatternView patternView, ExpresserCanvas expresserCanvas) {
        ExpresserModel model = expresserCanvas.getModel();
        PatternShape patternShape = patternView.getPatternShape(expresserCanvas);
        model.removeObject(patternShape);
        GroupShapeView buildingBlock = patternView.getBuildingBlock();
        PatternShape buildingBlockShape = buildingBlock.getPatternShape(expresserCanvas);
        model.addObject(buildingBlockShape);
    }

    public void groupUnmade(GroupShapeView groupShapeView, List<ShapeView> subShapeViews, ExpresserCanvas expresserCanvas) {
        ExpresserModel model = expresserCanvas.getModel();
        PatternShape groupShape = groupShapeView.getPatternShape(expresserCanvas);
        model.removeObject(groupShape);
        for (ShapeView subShapeView : subShapeViews) {
            PatternShape subShape = subShapeView.getPatternShape(expresserCanvas);
            model.addObject(subShape);
        }
    }

    public void positivitySet(PatternView patternView, boolean positive) {
        String id = patternView.getId();
        if (id == null) {
            Utilities.severe("id is null for setting the positivity/negativity of a shape");
            return;
        }
        BlockShape patternShape = patternView.getPatternShape();
        if (patternShape != null) {
            patternShape.setPositive(positive);
        }
        putPatternInDataStore(patternView);
    }

    /**
     * @param expression
     * @param id
     */
    public void associateExpressionWithId(LocatedExpression<IntegerValue> expression, String id) {
        idToExpressionMap.put(id, expression);
    }

    public LocatedExpression<IntegerValue> getExpression(ExpressionPanel expressionPanel) {
        return idToExpressionMap.get(expressionPanel.getId());
    }
}
