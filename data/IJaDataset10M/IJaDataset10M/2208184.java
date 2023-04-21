// Copyright 2007 Konrad Twardowski
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//   http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.makagiga.console;

import static org.makagiga.commons.UI._;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.text.ParseException;
import java.util.Map;
import javax.swing.AbstractCellEditor;
import javax.swing.DefaultCellEditor;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import org.makagiga.commons.ColorProperty;
import org.makagiga.commons.Config;
import org.makagiga.commons.EnumProperty;
import org.makagiga.commons.FontProperty;
import org.makagiga.commons.Item;
import org.makagiga.commons.MLogger;
import org.makagiga.commons.Property;
import org.makagiga.commons.ValueEvent;
import org.makagiga.commons.ValueListener;
import org.makagiga.commons.WTFError;
import org.makagiga.commons.mv.BooleanRenderer;
import org.makagiga.commons.mv.ColorEditor;
import org.makagiga.commons.mv.ColorRenderer;
import org.makagiga.commons.mv.MRenderer;
import org.makagiga.commons.swing.AbstractListTableModel;
import org.makagiga.commons.swing.MComboBox;
import org.makagiga.commons.swing.MFontButton;
import org.makagiga.commons.swing.MMessage;
import org.makagiga.commons.swing.MTable;

class ConsoleConfig extends MTable<AbstractListTableModel<?>> implements Config.GlobalEntry {

	// private
	
	private final BooleanRenderer booleanRenderer;
	private final ColorRenderer<Object> colorRenderer;
	private final MRenderer<Font> fontRenderer;
	private final MRenderer<Object> renderer;
	
	// public
	
	public ConsoleConfig() {
		super(new Model());

		getColumnManager().setDefaultColumnOrder(
			Model.NAME_COLUMN,
			Model.VALUE_COLUMN,
			Model.DEFAULT_VALUE_COLUMN,
			Model.TYPE_COLUMN
		);
		getColumnManager().updateProperties();

		setAutoCreateRowSorter(true);
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		booleanRenderer = new BooleanRenderer();
		booleanRenderer.setAutoDisable(true);

		colorRenderer = new ColorRenderer<>();
		colorRenderer.setFlat(true);
		
		fontRenderer = new MRenderer<Font>() {
			@Override
			protected void onRender(final Font f) {
				this.setText(FontProperty.toString(f));
				
				// bold modified properties
				ConsoleConfig table = (ConsoleConfig)this.getTable();
				Value value = (Value)table.getModel().getRowAt(this.getModelRow());
				this.setStyle(value.getValue().isDefaultValue() ? "font-weight: normal" : "font-weight: bold");
			}
		};

		renderer = new MRenderer<Object>() {
			@Override
			protected void onRender(final Object o) {
				this.setText(String.valueOf(o));
				
				// bold modified properties
				ConsoleConfig table = (ConsoleConfig)this.getTable();
				Value value = (Value)table.getModel().getRowAt(this.getModelRow());
				this.setStyle(value.getValue().isDefaultValue() ? "font-weight: normal" : "font-weight: bold");
			}
		};
	}

	@Override
	public TableCellEditor getCellEditor(final int row, final int column) {
		int modelColumn = convertColumnIndexToModel(column);
		if ((modelColumn == Model.VALUE_COLUMN) || (modelColumn == Model.DEFAULT_VALUE_COLUMN)) {
			int modelRow = convertRowIndexToModel(row);
			Value data = (Value)getModel().getRowAt(modelRow);
			Class<?> type = data.getValue().getType();
			Object value = getValueAt(row, column);
			if (type == Boolean.class) {
				BooleanRenderer booleanEditor = new BooleanRenderer();
				booleanEditor.setSelected((Boolean)value);
				
				return new DefaultCellEditor(booleanEditor);
			}
			else if (type == Color.class) {
				ColorEditor colorEditor = new ColorEditor();
				if (modelColumn == Model.VALUE_COLUMN) {
					Color defaultValue = (Color)getValueAt(row, Model.DEFAULT_VALUE_COLUMN);
					colorEditor.setDefaultValue(defaultValue);
				}
				
				return colorEditor;
			}
			else if (value instanceof Enum<?>) { // the type for EnumProperty may be null
				Value wrapper = (Value)getModel().getRowAt(modelRow);
				Property<?> p = wrapper.getValue();
				EnumProperty<?> enumProperty = (EnumProperty<?>)p;
				MComboBox<Enum<?>> enumEditor = new MComboBox<>();
				Enum<?>[] enumValues = enumProperty.getEnumValues();
				if (enumValues != null) {
					enumEditor.addAllItems(enumValues);
					enumEditor.setSelectedItem(enumProperty.get());
				}
				
				return new DefaultCellEditor(enumEditor);
			}
			else if (type == Font.class) {
				return new FontEditor();
			}
		}
		
		return super.getCellEditor(row, column);
	}

	@Override
	public TableCellRenderer getCellRenderer(final int row, final int column) {
		int modelColumn = convertColumnIndexToModel(column);
		if ((modelColumn == Model.VALUE_COLUMN) || (modelColumn == Model.DEFAULT_VALUE_COLUMN)) {
			int modelRow = convertRowIndexToModel(row);
			Value data = (Value)getModel().getRowAt(modelRow);
			Class<?> type = data.getValue().getType();
			Object value = data.getValue().get();
			if (type == Boolean.class) {
				return booleanRenderer;
			}
			else if (type == Color.class) {
				Color c = (Color)value;
				colorRenderer.setColor(c);
				colorRenderer.setText(ColorProperty.toString(c));
				
				return colorRenderer;
			}
			else if (type == Font.class) {
				return fontRenderer;
			}
		}
		
		return renderer;
	}

	public void updateModel() {
		Model model = (Model)getModel();
		model.update();
	}

	// Config.GlobalEntry

	/**
	 * @inheritDoc
	 *
	 * @since 3.8
	 */
	@Override
	public String getGlobalEntry(final String key) {
		return "ConsoleConfig." + key;
	}
	
	// private classes
	
	private static final class FontEditor extends AbstractCellEditor implements TableCellEditor {
	
		// private

		private Object value;
		
		// public

		@Override
		public Object getCellEditorValue() { return value; }

		@Override
		public Component getTableCellEditorComponent(final JTable table, final Object value, final boolean isSelected, final int row, final int column) {
			this.value = value;

			MFontButton editorComponent = new MFontButton((Font)value);
			editorComponent.addValueListener(new ValueListener<Font>() {
				public void valueChanged(final ValueEvent<Font> e) {
					FontEditor.this.value = e.getNewValue();
					FontEditor.this.stopCellEditing();
				}
			} );

			return editorComponent;
		}
		
		// private
		
		private FontEditor() { }

	}

	private static final class Model extends AbstractListTableModel<Value> {
		
		// private
		
		private static final int NAME_COLUMN = 0;
		private static final int VALUE_COLUMN = 1;
		private static final int DEFAULT_VALUE_COLUMN = 2;
		private static final int TYPE_COLUMN = 3;

		// public
		
		@Override
		public Object getValueAt(final int row, final int column) {
			Value value = getRowAt(row);
			switch (column) {
				case NAME_COLUMN:
					return value.getText();
				case VALUE_COLUMN:
					return value.getValue().get();
				case DEFAULT_VALUE_COLUMN:
					return value.getValue().getDefaultValue();
				case TYPE_COLUMN: {
					Property<?> p = value.getValue();
					StringBuilder s = new StringBuilder(Property.getTypeAsString(p));

					if (MLogger.isDeveloper()) {
						if (p.isReadOnly())
							s.append(", RO");
						if (p.isSecure(Property.SECURE_READ))
							s.append(", SR");
						if (p.isSecure(Property.SECURE_WRITE))
							s.append(", SW");
					}
					
					if (p instanceof EnumProperty<?>) {
						s
							.insert(0, "Enum (")
							.append(')');
					}

					return s.toString();
				}
				default:
					throw new WTFError("Unknown column: " + column);
			}
		}
		
		@Override
		public void setValueAt(final Object newValue, final int row, final int column) {
			if (column == VALUE_COLUMN) {
				Value value = getRowAt(row);
				try {
					if (value.getText().startsWith("Kiosk.") && !MMessage.simpleConfirm(null))
						return;
					
					Value before = createCopyForUndo(value);
					if (
						(newValue instanceof Boolean) ||
						(newValue instanceof Color) ||
						(newValue instanceof Enum<?>) ||
						(newValue instanceof Font)
					) {
						value.getValue().set(newValue);
					}
					else {
						value.getValue().parse(newValue.toString());
					}
					fireTableRowsUpdated(row, row);
					
					if (!value.getValue().equals(before.getValue())) // compare properties
						fireUndoableEditHappened(new ChangeUndo(before, createCopyForUndo(value), row));
				}
				catch (ParseException | SecurityException exception) {
					MMessage.error(null, exception);
				}
				
				Config.getDefault().sync();
			}
		}

		// protected
		
		@Override
		@SuppressWarnings("unchecked")
		protected Value createCopyForUndo(final Value original) {
			return new Value((Property<Object>)original.getValue().clone(), original.getText());
		}

		// private
		
		private Model() {
			super(
				new ColumnInfo(_("Name"), false, "NAME"),
				new ColumnInfo(_("Value"), true, "VALUE"),
				new ColumnInfo(_("Default Value"), false, "DEFAULT_VALUE"),
				new ColumnInfo(_("Type"), false, "TYPE")
			);
		}

		@SuppressWarnings("unchecked")
		private void update() {
			try {
				setEventsEnabled(false);
				clear();
				Config config = Config.getDefault();
				for (Map.Entry<String, Property<?>> i : config.getRegisteredProperties().entrySet())
					addRow(new Value((Property<Object>)i.getValue(), i.getKey()));
			}
			finally {
				setEventsEnabled(true);
				fireTableDataChanged();
			}
		}

	}

	private static final class Value extends Item<Property<Object>> {
		
		// private
		
		private Value(final Property<Object> value, final String text) {
			super(value, text);
		}
		
	}

}
