/*

Copyright (C) 2001 Kevin E. Gilpin (kevin.gilpin@alum.mit.edu)

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License (http://www.gnu.org/copyleft/gpl.html)
for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.

*/

package agonism.ce.cells;

import agonism.ce.Debug;
import agonism.ce.IPrintable;

import java.io.PrintWriter;

/**
 * This class represents represents a game board with an arbitrary number of rows and
 * columns, and an arbitrary cell geometry. Each location in the board is represented by
 * a {@link Cell}.
 *
 * @see HexagonalCellGeometry
 * @see SquareCellGeometry
 */
public class Board
	implements IPrintable,
			   java.io.Serializable
{
	public final static double VERY_SMALL = 1.0e-10;
	
	private final CellGeometry m_cellGeometry;
	private final Cell[][] m_cells;

	/**
	 * @param geometry cell geometry to use
	 * @param rows number of rows in the board
	 * @param cols number of columns in the board
	 */
	public Board(CellGeometry geometry, int rows, int cols)
	{
		Debug.assert(rows > 0 && cols > 0, "Board must have at least 1 row and column");

		m_cellGeometry = geometry;
		m_cells = new Cell[rows][cols];
		for ( int i=0;i<rows;i++ )
		{
			for ( int j=0;j<cols;j++ )
			{
				m_cells[i][j] = new Cell(i, j);
			}
		}
	}

	/**
	 * @return true if <code>other</code> is a Board, with the same number of rows and
	 * columns and the same geometry.
	 */
	public boolean equals(Object other)
	{
		if ( other instanceof Board )
			return equals((Board)other);
		else
			return false;
	}

	/**
	 * @return true if <code>other</code> has the same number of rows and
	 * columns and the same geometry.
	 */
	public boolean equals(Board other)
	{
		if ( this == other )
			return true;
		if ( m_cellGeometry.getClass() != other.m_cellGeometry.getClass() )
			return false;
		if ( getRows() != other.getRows() || getCols() != other.getCols() )
			return false;
		return true;
	}

	/**
	 * Get the cell geometry of the board.
	 */
	public final CellGeometry getGeometry()
	{
		return m_cellGeometry;
	}

	/**
	 * Get the number of rows in the board.
	 */
	public final int getRows()
	{
		return m_cells.length;
	}

	/**
	 * Get the number of columns in the board.
	 */
	public final int getCols()
	{
		return m_cells[0].length;
	}

	/**
	 * Get the total number of cells in the board, which is equal to
	 * <code>getRows() * getCols()</code>
	 */
	public final int getNumCells()
	{
		return getRows() * getCols();
	}

	/**
	 * Get the total width of the board. This is determined by the number of columns,
	 * and by the cell geometry.
	 */
	public double getWidth()
	{
		return m_cellGeometry.getTotalWidth(getCols());
	}

	/**
	 * Get the total heights of the board. This is determined by the number of rows,
	 * and by the cell geometry.
	 */
	public double getHeight()
	{
		return m_cellGeometry.getTotalHeight(getRows());
	}
	
	/**
	 * Get an array of Direction objects, each of which represents a direction in which it
	 * is possible to move from one cell to another. For example, for square cells there are 4 directions:
	 * {@link Direction#North North}, {@link Direction#South South}, {@link Direction#East East},
	 * and {@link Direction#West West}. Each object in the result array should be a constant value defined
	 * in {@link Direction}.
	 */
	public final Direction[] getDirections()
	{
		return m_cellGeometry.getDirections();
	}

	/**
	 * Get a random Cell from the board.
	 */
	public Cell getRandomCell()
	{
		int rows = getRows();
		int cols = getCols();
		int row = (int)( Math.random() * rows );
		int col = (int)( Math.random() * cols );
		return new Cell(row, col);
	}
	
	/**
	 * Get a random direction from among {@link #getDirections}.
	 */
	public final Direction randomDirection()
	{
		Direction[] directions = getDirections();
		int numDirections = directions.length;
		int direction = (int)(Math.random() * numDirections);
		return directions[direction];
	}

	/**
	 * Given a starting cell and and ending cell, get the angle of elevation
	 * in radians of the line segment which connects them. For example, if <code>end</code>
	 * is East of <code>start</code>, then the angle is <code>0</code>. If <code>end</code>
	 * is North of <code>start</code>, then the angle is <code>PI / 2</code>.
	 */
	public final double getDirectionRadians(Cell start, Cell end)
	{
		return m_cellGeometry.getDirectionRadians(start, end);
	}

	/**
	 * Get the angle of elevation in radians of a given integer direction code.
	 * @see #getDirections
	 */
	public final double getDirectionRadians(Direction direction)
	{
		return m_cellGeometry.getDirectionRadians(direction);
	}

	/**
	 * Get the index in the array of directions which contains a given direction.
	 * For example, if {@link #getDirections} returns an array containing
	 * <code>{ Direction.North, Direction.South, Direction.East, Direction.West }</code>, then
	 * <code>board.getDirectionIndex(Direction.South)</code> would return <code>1</code> and
	 * <code>board.getDirectionIndex(Direction.West)</code> would return <code>3</code>.
	 */
	public final int getDirectionIndex(Direction direction)
	{
		return m_cellGeometry.getDirectionIndex(direction);
	}

	/**
	 * @return true if <code>cell</code> is a valid cell on the board. A Cell
	 * is valid if its row and column are within the bounds of the board.
	 */
	public final boolean isValid(Cell location)
	{
		return isValid(location.row, location.col);
	}

	/**
	 * @return true if <code>row</code> and <code>col</code> are within the bounds
	 * of the board.
	 */
	public final boolean isValid(int row, int col)
	{
		return
			row >= 0 && row < getRows() &&
			col >= 0 && col < getCols();
	}

	/**
	 * Convert a pair of physical coordinates into a {@link Cell}. This method is useful
	 * for applications which use continuous coordinates layered on top of a Board.
	 */
	public final Cell getCell(double x, double y)
	{
		Cell cellLocation = m_cellGeometry.getLocation(x, y);
		if ( isValid(cellLocation) )
		{
			return cellLocation;
		}
		else
		{
		}
		return null;
	}

	/**
	 * Get a Cell from a row and column. The primary reason to use this function rather than constructing
	 * the Cell directly is that this method will always check that the row and column are valid. 
	 *
	 * @exception IllegalArgumentException if row or column are invalid
	 * @see #isValid
	 */
	public final Cell getCell(int row, int col)
	{
		Cell cell = new Cell(row, col);
		if ( !isValid(cell) )
		{
			throw new IllegalArgumentException("Row, column (" + row + ", " + col + ") are not valid coordinates on this Board");
		}
		return cell;
	}
	
	/**
	 * Get a Cell from a single index value. If all the cells are numbered from 0, moving across
	 * by columns and then down by rows at the end of each column, this method will return
	 * the cell whose number is <code>index</code>.
	 *
	 * @exception IllegalArgumentException if row or column are invalid
	 * @see #isValid
	 */
	public final Cell getCell(int index)
	{
		int row = index / getCols();
		int col = index % getCols();
		Cell cell = new Cell(row, col);
		if ( !isValid(cell) )
		{
			throw new IllegalArgumentException("Row, column (" + row + ", " + col + ") are not valid coordinates on this Board");
		}
		return cell;
	}
	
	/**
	 * Given a {@link Cell}, and a direction to move from that location, return the
	 * adjacent {@link Cell} in that direction.
	 */
	public final Cell getCell(Cell location, Direction direction)
	{
		int row = m_cellGeometry.getRow(location, direction);
		int col = m_cellGeometry.getColumn(location, direction);

		return new Cell(row, col);
	}
	
	/**
	 * @return the distance between two locations.
	 */
	public double distance(Cell start, Cell end)
	{
		return m_cellGeometry.distance(start, end);
	}
	
	/**
	 * This method can be used to determine the best direction to move to get
	 * from one cell to another.
	 * @return the direction which is closest to the angle of the line segment
	 * connecting <code>start</code> and <code>end</code>.
	 * @see #getDirections
	 */
	public Direction direction(Cell start, Cell end)
	{
		return m_cellGeometry.direction(start, end);
	}

	/**
	 * Get a string representation of the board.
	 */
	public String toString()
	{
		return Debug.printString(this);
	}
	
	public void toString(PrintWriter writer)
	{
		writer.print("Board [");
		writer.print("Rows = ");writer.print(getRows());
		writer.print(", Cols = ");writer.print(getCols());
		writer.print(", Width = ");writer.print(getWidth());
		writer.print(", Height = ");writer.print(getHeight());
		writer.print(", Geometry = ");writer.print(m_cellGeometry);
		writer.print(" ]");
	}
}
