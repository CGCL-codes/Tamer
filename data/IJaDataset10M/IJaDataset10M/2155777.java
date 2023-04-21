// Copyright 2005 Konrad Twardowski
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

package org.makagiga.search;

import java.text.ParseException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.makagiga.commons.BooleanProperty;
import org.makagiga.commons.ColorProperty;
import org.makagiga.commons.MArrayList;
import org.makagiga.commons.MLogger;
import org.makagiga.commons.Property;
import org.makagiga.commons.StringList;
import org.makagiga.commons.TK;
import org.makagiga.commons.category.CategoryList;
import org.makagiga.commons.mv.MV;
import org.makagiga.commons.swing.MTreeModel;
import org.makagiga.fs.AbstractFS;
import org.makagiga.fs.FSQuery;
import org.makagiga.fs.MetaInfo;
import org.makagiga.tags.Tags;
import org.makagiga.tags.TagsUtils;
import org.makagiga.tree.Tree;

// TODO: search by date
public final class Query {
	
	// private
	
	private boolean filterTree;
	private boolean processSearchFS = true;
	
	// public
	
	public enum Operator {
		CONTAINS_IGNORE_CASE,
		EQUALS,

		/**
		 * @since 4.0
		 */
		EQUALS_IGNORE_CASE

	};
	
	// keys
	
	/**
	 * @since 2.0
	 */
	public static final String AND = "AND";

	/**
	 * @since 3.8.10
	 */
	public static final String OR = "OR";

	public static final String BOOKMARK = "bookmark";
	
	/**
	 * @since 3.0
	 */
	public static final String CATEGORY = "category";
	
	/**
	 * @since 4.2
	 */
	public static final String COLOR = "color";

	public static final String ICON = "icon";
	public static final String KEYWORDS = "keywords";
	public static final String RATING = "rating";
	public static final String TAG = "tag";
	public static final String TYPE = "type";

	// private

	private boolean allVisible;
	private boolean matchAll;
	private Hits list;
	private final Map<String, Criterium> criteria = new LinkedHashMap<>();
	private SortMethod sortMethod;
	
	// public

	public Query(final SortMethod sortMethod) {
		this(false, sortMethod);
	}
	
	public Query(final String query, final SortMethod sortMethod) {
		this(false, sortMethod);
		parse(query);
	}
	
	public void add(final String key, final Object value) {
		add(key, Operator.EQUALS, value);
	}
	
	public void add(final String key, final Operator operator, final Object value) {
		criteria.put(key, new Criterium(operator, value));
	}
	
	/**
	 * @since 2.2
	 */
	public void add(final String key, final Operator operator, final String[] keywords) {
		add(key, operator, new StringList(keywords));
	}
	
	/**
	 * @since 2.0
	 */
	public static Hits all(final SortMethod sortMethod) {
		Query query = new Query(true, sortMethod);
		
		return query.start();
	}

	/**
	 * @since 2.0
	 */
	public Map<String, Criterium> getCriteria() { return criteria; }
	
	public boolean isAllVisible() { return allVisible; }
	
	public boolean matchesKeywords(final MetaInfo metaInfo, final Criterium criterium) {
		StringList keywords = criterium.getKeywords();
		for (String keyword : keywords) {
			if (matchesKeyword(metaInfo, keyword))
				return true;
		}
		
		return false;
	}
	
	public void setFilterTree(final boolean value) { filterTree = value; }
	
	public void setProcessSearchFS(final boolean value) { processSearchFS = value; }

	public Hits sort(final SortMethod sortMethod) {
		list.sort(sortMethod.getMetaInfoComparator());

		return list;
	}
	
	public Hits start() {
		final Tree tree = Tree.getInstance();
		MTreeModel<MetaInfo> model = tree.getModel();

		if (filterTree)
			model.setFilterMode(MV.MODEL);
			
		final BooleanProperty inTrash = new BooleanProperty();
		final List<MetaInfo> expand = new MArrayList<>();
		allVisible = true;
		list = new Hits();
		for (final AbstractFS fs : tree) {
			inTrash.set("trash".equals(fs.getID()));
			new Tree.Scanner(fs) {
				@Override
				public void processItem(final MetaInfo metaInfo) {
					if ((isSearchFS(fs) && !processSearchFS) || metaInfo.isLink())
						return;

					boolean matches = matches(metaInfo, inTrash.get());

					if (!matches && (fs instanceof FSQuery))
						matches = FSQuery.class.cast(fs).matches(Query.this, metaInfo);

					if (matches) {
						if (!list.contains(metaInfo)) {
							list.add(metaInfo);
							if (filterTree)
								metaInfo.setVisible(true);
						}
					}
					else {
						if (filterTree) {
							allVisible = false;
							metaInfo.setVisible(false);
						}
					}
				}
				@Override
				public void processParent(final MetaInfo metaInfo) {
					if (!filterTree)
						return;

					if (isSearchFS(fs) && !processSearchFS)
						return;

					if (!metaInfo.isFSRoot()) {
						if (
							!metaInfo.isEmpty(MV.VIEW) ||
							list.contains(metaInfo)
						) {
							metaInfo.setVisible(true);
							expand.add(metaInfo);
						}
						else {
							allVisible = false;
							metaInfo.setVisible(false);
						}
					}
				}
				private boolean isSearchFS(final AbstractFS fs) {
					return (fs != null) && "search".equals(fs.getID());
				}
			};
		}
		
		if (filterTree) {
			// show all items
			if (allVisible || list.isEmpty()) {
				model.setFilterMode(MV.MODEL);
				model.setAllVisible(true);
			}
			else {
				model.setFilterMode(MV.VIEW);
			}
			model.reload();
			
			for (MetaInfo i : expand)
				tree.setExpanded(i, true);
		}
		
		if (sortMethod != SortMethod.UNSORTED)
			sort(sortMethod);

		return list;
	}
	
	// private

	private Query(final boolean matchAll, final SortMethod sortMethod) {
		this.matchAll = matchAll;
		this.sortMethod = sortMethod;
	}

	private boolean matches(final MetaInfo metaInfo, final boolean inTrash) {
		// all
		if (matchAll)
			return true;

		// cache
		List<String> categoryList = null;
		Tags tags = null;

		boolean or = criteria.containsKey(OR);
		boolean matches = false;
		String key;
		Criterium criterium;
		for (Map.Entry<String, Criterium> i : criteria.entrySet()) {
			key = i.getKey();
			criterium = i.getValue();
			
			// AND operator
			if (AND.equals(key)) {
				matches = false;
				
				continue; // for
			}

			// match any
			if (criterium.isNull())
				continue; // for
			
			switch (key) {

			case BOOKMARK: {
				if (!inTrash && criterium.matches(metaInfo.isBookmark()))
					matches = true;
			} break;

			case CATEGORY: {
				if (!inTrash) {
					if (categoryList == null)
						categoryList = CategoryList.toList(metaInfo.getCategories());
					if (!categoryList.isEmpty()) {
						for (String category : categoryList) {
							if (criterium.matches(category)) {
								matches = true;

								break; // for
							}
						}
					}
				}
			} break;
			
			case COLOR: {
				if (criterium.matches(metaInfo.getColor()))
					matches = true;
			} break;

			case ICON: {
				String iconName = metaInfo.getIconName();
				if ((iconName != null) && criterium.matches(iconName))
					matches = true;
			} break;
			
			case RATING: {
				if (criterium.matches(metaInfo.getRating()))
					matches = true;
			} break;
		
			case TAG: {
				if (!inTrash) {
					if (tags == null)
						tags = TagsUtils.removeDuplicates(metaInfo.getTags());
					if (criterium.matches("")) {
						if (tags.isEmpty())
							matches = true;
					}
					else {
						for (String tag : tags) {
							if (criterium.matches(tag, Operator.EQUALS_IGNORE_CASE)) {
								matches = true;

								break; // for
							}
						}
					}
				}
			} break;
			
			case TYPE: {
				String extension = metaInfo.getFileExtension();
				if (!extension.isEmpty() && criterium.matches(extension))
					matches = true;
			} break;
			
			case KEYWORDS: {
				if (matchesKeywords(metaInfo, criterium))
					matches = true;
			} break;
			} // switch

			// nothing found - stop search
			if (!matches) {
				if (or) // test other hits
					continue; // for
				else
					break; // for
			}
			else {
				if (or) // one hit is enough
					break; // for
			}
		}
		
		return matches;
	}
	
	private boolean matchesKeyword(final MetaInfo metaInfo, final String keyword) {
		// match item name
		if (TK.containsIgnoreCase(metaInfo.toString(), keyword))
			return true;

		// match parent folder name
		String parentFolderName = metaInfo.getParentFolder().toString();
		if (TK.containsIgnoreCase(parentFolderName, keyword))
			return true;

		// match category
		if (TK.containsIgnoreCase(metaInfo.getCategories(), keyword))
			return true;

		// match comment
		if (TK.containsIgnoreCase(metaInfo.getComment(), keyword))
			return true;

		// match keywords
		if (Index.getInstance().containsIgnoreCase(metaInfo, keyword))
			return true;

		// match tags
		if (TK.containsIgnoreCase(metaInfo.getTags(), keyword))
			return true;

		return false;
	}
	
	private void parse(final String query) {
		if (query == null)
			return;
		
// TODO: 2.0: improve highlight (global)
		
		List<String> args;
		try {
			args = TK.parseArguments(query);
		}
		catch (ParseException exception) {
			return;
		}

		String name;
		String value;
		StringList keywords = new StringList();
		for (String arg : args) {
			if (AND.equals(arg)) {
				add(AND, null);
				
				continue; // for
			}
			
			String[] pair = TK.splitPair(arg, '=', TK.SPLIT_PAIR_NULL_ERROR);
			if (pair == null) {
				name = null;
				value = null;
				
				keywords.add(arg);

				continue; // while
			}
			else {
				name = pair[0];
				if (name.isEmpty())
					name = null;
				value = pair[1];
				if (value.isEmpty())
					value = null;
			}
			
			//MLogger.debug("query", "arg=\"%s\", name=\"%s\", value=\"%s\"", arg, name, value);
			
			if ((name == null) || (value == null))
				continue; // for

			switch (name) {
			case BOOKMARK:
				add(BOOKMARK, Boolean.valueOf(value));
				break;
			case COLOR:
				try {
					add(name, ColorProperty.parseColor(value));
				}
				catch (ParseException exception) {
					if (MLogger.isDeveloper())
						MLogger.exception(exception);
				}
				break;
			case RATING:
				try {
					add(RATING, Float.valueOf(value));
				}
				catch (NumberFormatException exception) {
					if (MLogger.isDeveloper())
						MLogger.exception(exception);
				}
				break;
			case CATEGORY:
			case ICON:
			case TAG:
			case TYPE:
				add(name, value);
				break;
			} // switch
		}
		
		if (!keywords.isEmpty())
			add(KEYWORDS, Operator.CONTAINS_IGNORE_CASE, keywords.toArray());
	}
	
	// public classes
	
	public static final class Criterium extends Property<Object> {
		
		// private

		private Object[] values;
		private final Operator operator;
		
		// public
		
		public Criterium(final Operator operator, final Object value) {
			super(value);
			this.operator = operator;
			if (value instanceof String) {
				this.values = TK.fastSplit(value.toString(), '|').toArray();
				if (this.values.length == 0)
					this.values = new Object[] { "" };
			}
			else {
				this.values = new Object[1];
				this.values[0] = value;
			}
		}
		
		/**
		 * @since 2.2
		 */
		public StringList getKeywords() {
			return (StringList)get();
		}

		public boolean matches(final Object anotherValue) {
			return matches(anotherValue, operator);
		}

		// private

		private boolean matches(final Object anotherValue, final Operator operator) {
			for (Object i : values) {
				switch (operator) {
					case CONTAINS_IGNORE_CASE:
						if (TK.containsIgnoreCase(i.toString(), anotherValue.toString()))
							return true;
						
						continue; // for
					case EQUALS:
						if (i.equals(anotherValue))
							return true;
						
						continue; // for
					case EQUALS_IGNORE_CASE:
						if ((i instanceof String) && (anotherValue instanceof String)) {
							if (String.class.cast(i).equalsIgnoreCase((String)anotherValue))
								return true;
						}

						continue; // for
					default:
						throw new IllegalArgumentException("Unknown operator: " + operator);
				}
			}
			
			return false;
		}

	}
	
	public static final class Hits extends MArrayList<MetaInfo> { }
	
}
