package com.inature.oce.core.api.nom;

import java.util.List;

/**
 * Copyright 2007 i-nature
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * Provides access to group definitions specified in groups.xml
 * 
 * @author Yavor Mitkov Gologanov
 *
 */
public interface CategoryProvider {

    /**
	 * 
	 * @param name
	 * @return Category
	 */
    public Category getCategory(String name);

    /**
	 * Returns a list that contains all available categories.
	 * 
	 * @return List<Category>
	 */
    public List<Category> getCategories();
}
